package com.mad.delivery.bikerApp.orders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.FirebaseCallbackItem;
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;

public class DetailOrderActivity extends AppCompatActivity {
    Toolbar myToolBar;
    private ViewPager mPager;
    private DetailOrderPageAdapter pagerAdapter;
    private TabLayout tabLayout;
    private Order order;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        mPager = findViewById(R.id.detail_order_pager);

        order  = bundle.getParcelable("order");

        if (order == null) {
            String orderId = bundle.getString("extra");
            if (orderId != null) {
                BikerDatabase.getInstance().getOrderById(orderId, order -> {
                    DetailOrderActivity.this.order = order;
                    loadOrder(order);
                });
                return;
            }
        }

        loadOrder(order);
    }

    void loadOrder(Order order) {
        if (pagerAdapter == null) {
            pagerAdapter = new DetailOrderPageAdapter(getSupportFragmentManager(), this, order);
            mPager.setAdapter(pagerAdapter);
        }
        pagerAdapter.order = order;
        pagerAdapter.notifyDataSetChanged();
        setTitle(getResources().getString(R.string.order_from_title) + " " + order.client.name + " " + order.client.lastName);
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
        intent.putExtra("open", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(order.status.equals(OrderStatus.completed)) {
            getMenuInflater().inflate(R.menu.order_detail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.save_order_option:
                Log.d("MADAPP", "Save option selected");
                item.setTitle("SEND");
                Intent intent = new Intent(getApplicationContext(), CompletingOrderActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
