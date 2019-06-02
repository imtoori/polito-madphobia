package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;

import org.joda.time.DateTime;


public class OrderInfoActivity extends AppCompatActivity {
    Order order;
    TextView order_code;
    TextView subtot;
    TextView del_fee;
    TextView tot;
    TextView address;
    TextView pay_meth;
    TextView data;
    TextView rest_note;
    TextView biker_note;
    TextView client_note;
    Toolbar toolbar;
    CardView cv_biker_note, cv_rest_note, cv_client_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = getIntent().getParcelableExtra("order");
        setContentView(R.layout.activity_order_info);
        setTitle(getResources().getString(R.string.order_info_toolbar));
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        order_code=findViewById(R.id.order_code);
        subtot=findViewById(R.id.subtotal_price);
        del_fee=findViewById(R.id.delivery_fee);
        tot=findViewById(R.id.total);
        address=findViewById(R.id.address);
        pay_meth=findViewById(R.id.payment_method);
        data=findViewById(R.id.data);
        rest_note=findViewById(R.id.rest_note);
        biker_note=findViewById(R.id.biker_note);
        client_note=findViewById(R.id.client_note);
        cv_biker_note=findViewById(R.id.cv_biker_note);
        cv_rest_note=findViewById(R.id.cv_restaurant_note);
        cv_client_note=findViewById(R.id.cv_client_note);
        order_code.setText(order.id);
        Double sub= order.totalPrice-order.restaurant.previewInfo.deliveryCost;
        subtot.setText(sub.toString());
        del_fee.setText(order.restaurant.previewInfo.deliveryCost.toString());
        tot.setText(order.totalPrice.toString());
        address.setText(order.delivery);
        pay_meth.setText(order.paymentMethod);
        data.setText(MyDateFormat.parse(new DateTime(order.orderFor)));

        if(order.serverNotes!="" && order.serverNotes!=null) {
            rest_note.setText(order.serverNotes);
            cv_rest_note.setVisibility(View.VISIBLE);
        }
        else
            cv_rest_note.setVisibility(View.GONE);

        if(order.bikerNotes!="" && order.bikerNotes!=null){
            biker_note.setText(order.bikerNotes);
            cv_biker_note.setVisibility(View.VISIBLE);
        }
        else
            cv_biker_note.setVisibility(View.GONE);

        if(order.clientNotes!="" && order.clientNotes!=null){
            client_note.setText(order.clientNotes);
            cv_client_note.setVisibility(View.VISIBLE);
        }
        else
            cv_client_note.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        SummaryOrdersAdapter summaryordersAdapter = new SummaryOrdersAdapter(order.products);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(summaryordersAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("open", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




}

