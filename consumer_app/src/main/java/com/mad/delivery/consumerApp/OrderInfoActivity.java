package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;


public class OrderInfoActivity extends AppCompatActivity {
    Menu menu;
    Order order;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    TextView rest_note;
    TextView biker_note;
    TextView client_note;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = ConsumerDatabase.getInstance().getOrder();
        setContentView(R.layout.activity_order_info);
        setTitle(getResources().getString(R.string.order_info_toolbar));
        toolbar=findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.Basket_toolbar));
        setSupportActionBar(toolbar);
        order_code=findViewById(R.id.order_code);
        subtot=findViewById(R.id.subtotal_price);
        del_fee=findViewById(R.id.delivery_fee);
        tot=findViewById(R.id.total);
        rest_note=findViewById(R.id.rest_note);
        biker_note=findViewById(R.id.biker_note);
        client_note=findViewById(R.id.client_note);
        order_code.setText(order.id);
        subtot.setText(order.totalPrice.toString());
        //TODO insert deliveryCost
        del_fee.setText("0.00");
        tot.setText(order.totalPrice.toString());
        //TODO insert bikernote
        rest_note.setText(order.serverNotes);
        biker_note.setText("");
        client_note.setText(order.clientNotes);

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        SummaryOrdersAdapter summaryordersAdapter = new SummaryOrdersAdapter(order.products);
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

