package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Product;


public class OrderInfoActivity extends AppCompatActivity {
    Menu menu;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    TextView rest_note;
    TextView biker_note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        setTitle(getResources().getString(R.string.profile_toolbar));
        order_code=findViewById(R.id.order_code);
        subtot=findViewById(R.id.subtotal_price);
        del_fee=findViewById(R.id.delivery_fee);
        tot=findViewById(R.id.total);
        rest_note=findViewById(R.id.rest_note);
        biker_note=findViewById(R.id.biker_note);

        order_code.setText("#123456");
        subtot.setText("0.00");
        del_fee.setText("0.00");
        tot.setText("0.00");
        rest_note.setText("note ristorante bla bla");
        biker_note.setText("note biker bla bla");


        List<Product> products = new ArrayList<>();
        Product p1 = new Product("Prodotto 1", 20, 18.50);
        Product p2 = new Product("Prodotto 2", 10, 17.01);
        products.add(p1);
        products.add(p2);

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        SummaryOrdersAdapter summaryordersAdapter = new SummaryOrdersAdapter(products);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(summaryordersAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("user", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }


}

