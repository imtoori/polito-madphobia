package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Product;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_layout);
        toolbar=findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.basket_toolbar));
        setSupportActionBar(toolbar);
        order_code=findViewById(R.id.order_code);
        subtot=findViewById(R.id.subtotal_price);
        del_fee=findViewById(R.id.delivery_fee);
        tot=findViewById(R.id.total);
        address=findViewById(R.id.address);
        time=findViewById(R.id.time);
        payment=findViewById(R.id.button);
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
        payment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO store order
            }
        });

        order_code.setText("#123456");
        subtot.setText("0.00");
        del_fee.setText("0.00");
        tot.setText("0.00");


        List<Product> products = new ArrayList<>();
        Product p1 = new Product("Prodotto 1", 20, 18.50);
        Product p2 = new Product("Prodotto 2", 10, 17.01);
        products.add(p1);
        products.add(p2);
        products.add(p1);
        products.add(p2);
        products.add(p1);
        products.add(p2);

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        BasketAdapter basketAdapter = new BasketAdapter(products);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(basketAdapter);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
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



}