package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Basket extends AppCompatActivity {
    Menu menu;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    TextView restaurant;
    Toolbar toolbar;

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

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("user", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }


}