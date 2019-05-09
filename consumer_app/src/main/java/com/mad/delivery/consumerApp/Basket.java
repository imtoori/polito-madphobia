package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import static java.security.AccessController.getContext;


public class Basket extends AppCompatActivity implements TimePickerFragment.TimePickedListener {
    Menu menu;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    EditText address;
    TextView time;
    String payment_met;
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
    TextView pm;
    RadioButton credit;
    private FirebaseAuth mAuth;



    interface ClickListener {
        void onItemClicked(Double position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.basket_layout);
        toolbar = findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.Basket_toolbar));
        setSupportActionBar(toolbar);
        order_code = findViewById(R.id.order_code);
        subtot = findViewById(R.id.subtotal_price);
        del_fee = findViewById(R.id.delivery_fee);
        tot = findViewById(R.id.total);
        address = findViewById(R.id.address);
        time = findViewById(R.id.time);
        payment = findViewById(R.id.button);
        notes = findViewById(R.id.client_note);
        rg = findViewById(R.id.rg_method);
        pm = findViewById(R.id.pm);
        credit= findViewById(R.id.credit);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.credit) {
                    payment_met = "credit";
                } else {
                    payment_met = "cash";
                }
            }

        });


        List<Product> products = new ArrayList<>();

        ConsumerDatabase.getInstance().getItemSelected().forEach((item, value) -> products.add(new Product(item.name, value, item.price)));

        priceD = 0.0;
        fee = 0.0;
        products.forEach(p -> priceD += p.price * p.quantity);
        //TODO insert real OrderID
        order_code.setText("#123456");
        totD = priceD + fee;
        subtot.setText(priceD.toString());
        del_fee.setText(fee.toString());
        tot.setText(totD.toString());

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        BasketAdapter basketAdapter = new BasketAdapter(products, new ClickListener() {
            @Override
            public void onItemClicked(Double position) {
                priceD -= position;
                subtot.setText(priceD.toString());
                totD = priceD + fee;
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
                if(checkConstraints()){
                ConsumerDatabase.getInstance().getUserId(new firebaseCallback<User>() {
                                               @Override
                                               public void onCallBack(User item) {
                                                   if (item != null && item.lastName != null) {
                                                       order = new Order(item, ConsumerDatabase.getInstance().getRestaurantInLocal(), products, address.toString(), payment_met);
                                                       if (order.totalPrice <= item.credit && payment_met == "credit") {
                                                           ConsumerDatabase.getInstance().putOrder(order);
                                                           ConsumerDatabase.getInstance().updateCreditCustomer(-order.totalPrice, new firebaseCallback<Boolean>() {
                                                               @Override
                                                               public void onCallBack(Boolean item) {
                                                                   if (item) {
                                                                       Log.i("TAG", "Transazione Avvenuta con successo");
                                                                       Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                                       startActivity(intent);
                                                                       overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                                   }
                                                                   else
                                                                       Log.i("TAG", "Transazione NON Avvenuta con successo");


                                                               }
                                                           });
                                                       } else if (payment_met == "cash") {
                                                           ConsumerDatabase.getInstance().putOrder(order);
                                                           Log.i("TAG", "Acquisto effettuato ");
                                                           Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                           startActivity(intent);
                                                           overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                       } else

                                                           Log.i("TAG", "Non hai abbastanza credito");



                                                   } else {
                                                       Log.i("TAG", "Ti devi registrare");
                                                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                       startActivity(intent);
                                                       overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                   }
                                               }
                                           });

                                       }
            }
                                   }

        );

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("user", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        datetime = DateTime.now();
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

    public void deleteProduct() {
        Toast.makeText(this, "ciao", Toast.LENGTH_SHORT).show();
    }

    public boolean checkConstraints() {
        boolean result = true;
        String checkString = "[a-zA-Z]+";
        String checkTime = "^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$";

        if(priceD<=0){
            tot.setError(getResources().getString(R.string.empty_basket_error));
            result=false;
        }

        if (!address.getText().toString().matches(checkString)) {
            address.setError(getResources().getString(R.string.check_address));
            result = false;
        }
        if (!time.getText().toString().matches(checkTime)) {
            time.setError(getResources().getString(R.string.check_time));
            result = false;
        }
        else
            time.setError(null);

        if(payment_met=="credit") {
            Log.i("TAG", "crdito");
            ConsumerDatabase.getInstance().getUserId(new firebaseCallback<User>() {
                @Override
                public void onCallBack(User user) {
                    if (user != null && user.credit < priceD) {
                        credit.setEnabled(false);
                        rg.check(R.id.cash);
                        payment_met = "cash";

                    }

                }

            });
        }

        return result;

    }
}