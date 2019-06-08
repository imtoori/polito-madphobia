package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.consumerApp.wallet.WalletFragment;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.PreviewInfo;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;


public class OrderInfoActivity extends AppCompatActivity  {
    Order order;
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
    CardView cv_biker_note, cv_rest_note, cv_client_note, cvFeedback;
    FeedBackFragment feedbackFragment;
    Button btnFeedback;
    SummaryOrdersAdapter summaryordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_info);
        setTitle(getResources().getString(R.string.order_info_toolbar));
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        subtot = findViewById(R.id.subtotal_price);
        del_fee = findViewById(R.id.delivery_fee);
        tot = findViewById(R.id.total);
        address = findViewById(R.id.address);
        pay_meth = findViewById(R.id.payment_method);
        data = findViewById(R.id.data);
        rest_note = findViewById(R.id.rest_note);
        biker_note = findViewById(R.id.biker_note);
        client_note = findViewById(R.id.client_note);
        cv_biker_note = findViewById(R.id.cv_biker_note);
        cv_rest_note = findViewById(R.id.cv_restaurant_note);
        cv_client_note = findViewById(R.id.cv_client_note);
        cvFeedback = findViewById(R.id.cv_feedback);
        btnFeedback = findViewById(R.id.btn_feedback);
        RecyclerView recyclerView = findViewById(R.id.rv_orders);
        summaryordersAdapter = new SummaryOrdersAdapter(new ArrayList<>());
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(summaryordersAdapter);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("id");

        Log.d("MADAPP", "order = " + orderId);
        if (orderId != null) {
            ConsumerDatabase.getInstance().getOrderById(orderId, item -> {
                order = item;
                loadOrder(item);
                btnFeedback.setOnClickListener(v -> {
                    openFeedbackDialog(order);

                });
            });
            return;
        }

        loadOrder(order);

    }

    private void loadOrder(Order order) {
        double sub = order.totalPrice - order.restaurant.previewInfo.deliveryCost;
        subtot.setText("€ " + sub);
        del_fee.setText("€ " + order.restaurant.previewInfo.deliveryCost.toString());
        tot.setText("€ " + order.totalPrice.toString());
        address.setText(order.delivery);
        pay_meth.setText(order.paymentMethod);
        data.setText(MyDateFormat.parse(new DateTime(order.orderFor)));

        if (order.serverNotes != null && !order.serverNotes.equals("")) {
            rest_note.setText(order.serverNotes);
            cv_rest_note.setVisibility(View.VISIBLE);
        } else
            cv_rest_note.setVisibility(View.GONE);

        if (order.bikerNotes != null && !order.bikerNotes.equals("")) {
            biker_note.setText(order.bikerNotes);
            cv_biker_note.setVisibility(View.VISIBLE);
        } else
            cv_biker_note.setVisibility(View.GONE);

        if (order.clientNotes != null && !order.clientNotes.equals("")) {
            client_note.setText(order.clientNotes);
            cv_client_note.setVisibility(View.VISIBLE);
        } else
            cv_client_note.setVisibility(View.GONE);

        if (order.status.equals(OrderStatus.delivered) && order.feedbackIsPossible) {
            cvFeedback.setVisibility(View.VISIBLE);
        } else {
            cvFeedback.setVisibility(View.GONE);
        }

        summaryordersAdapter.products = order.products;
        summaryordersAdapter.notifyDataSetChanged();
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

    public void openFeedbackDialog(Order o) {
        feedbackFragment = FeedBackFragment.newInstance(o);
        feedbackFragment.show(getSupportFragmentManager(), "feedbackFragment");
    }



}

