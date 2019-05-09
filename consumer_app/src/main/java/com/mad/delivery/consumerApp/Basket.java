package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;


public class Basket extends AppCompatActivity implements TimePickerFragment.TimePickedListener{
    Menu menu;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    EditText address;
    TextView time;
    Boolean payment_met;
    RadioGroup rg;
    Toolbar toolbar;
    DateTime datetime;
    Button payment;
    EditText notes;
    Order order;

    Customer customer;
    Double priceD;
    Double fee;
    Double totD;


    interface ClickListener {
        void onItemClicked(Double position);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_layout);
        toolbar=findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.Basket_toolbar));
        setSupportActionBar(toolbar);
        order_code=findViewById(R.id.order_code);
        subtot=findViewById(R.id.subtotal_price);
        del_fee=findViewById(R.id.delivery_fee);
        tot=findViewById(R.id.total);
        address=findViewById(R.id.address);
        time=findViewById(R.id.time);
        payment=findViewById(R.id.button);
        notes=findViewById(R.id.client_note);
        rg=findViewById(R.id.rg_method);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.credit) {
                    payment_met=true;
                } else {
                    payment_met=false;
                }
            }

        });



        List<Product> products = new ArrayList<>();

        ConsumerDatabase.getInstance().getItemSelected().forEach((item, value) -> products.add(new Product(item.name,value,item.price)));

         priceD=0.0;
         fee =0.0;
        products.forEach(p-> priceD+=p.price*p.quantity);
        order_code.setText("#123456");
        totD=priceD+fee;
        subtot.setText(priceD.toString());
        del_fee.setText(fee.toString());
        tot.setText(totD.toString());

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        BasketAdapter basketAdapter = new BasketAdapter(products, new ClickListener() {
            @Override
            public void onItemClicked(Double position) {
                priceD-=position;
                subtot.setText(priceD.toString());
                totD=priceD+fee;
                tot.setText(totD.toString());


            }
        });
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(basketAdapter);



        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConsumerDatabase.getInstance().getUserId(new firebaseCallback<User>() {
                    @Override
                    public void onCallBack(User item) {
                        if(item!=null && item.lastName!=null){
                            order =new Order(item,ConsumerDatabase.getInstance().getRestaurantInLocal(),products,"","cash");
                            if(order.totalPrice<=item.credit) {
                                ConsumerDatabase.getInstance().putOrder(order);
                                ConsumerDatabase.getInstance().updateCreditCustomer(-order.totalPrice, new firebaseCallback<Boolean>() {
                                    @Override
                                    public void onCallBack(Boolean item) {
                                        if(item)
                                            Log.d("TAG","Transazione Avvenuta con successo");
                                        else
                                            Log.d("TAG","Transazione NON Avvenuta con successo");


                                    }
                                });
                            }
                            else
                                Log.d("TAG","Non hai abbastanza credito");

                        }
                        else
                            Log.d("TAG","Ti devi registrare");
                    }
                });

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("user", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        datetime= DateTime.now();
        bundle.putSerializable("datetime", datetime);
        timeFragment.setArguments(bundle);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimePicked(int h, int m) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        datetime = datetime.hourOfDay().setCopy(h);
        datetime = datetime.minuteOfHour().setCopy(m);
        time.setText(datetime.toString(dtf));
    }

    public void deleteProduct()
    {
        Toast.makeText(this, "ciao", Toast.LENGTH_SHORT).show();
    }



}