package com.mad.delivery.bikerApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.orders.DetailOrderActivity;
import com.mad.delivery.bikerApp.orders.Order;
import com.mad.delivery.bikerApp.orders.OrderFragment;
import com.mad.delivery.bikerApp.orders.PendingOrdersFragment;
import com.mad.delivery.bikerApp.settings.SettingFragment;

public class HomeActivity extends AppCompatActivity implements PendingOrdersFragment.OnPendingOrderListener {
    Toolbar myToolBar;
    private Order orderToBeUpdated;
    int open = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("open")) {
            open = getIntent().getIntExtra("open", 0);
        } else {
            Log.d("MADAPP", "NO open..");
        }

        if(getIntent().hasExtra("orderToBeUpdated")) {
            Bundle bundle = getIntent().getExtras();
            orderToBeUpdated = bundle.getParcelable("orderToBeUpdated");
        }
        myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.app_name));
        BottomNavigationView navigation = findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                switch (item.getItemId()) {
                    case R.id.nav_orders:
                        setTitle(getString(R.string.nav_orders));
                        OrderFragment orderFragment = new OrderFragment();
                        ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        if(orderToBeUpdated != null) bundle.putParcelable("orderToBeUpdated", orderToBeUpdated);
                        orderFragment.setArguments(bundle);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.replace(R.id.frag_container, orderFragment);
                        ft.commit();
                        return true;
                    case R.id.nav_settings:
                        setTitle(getString(R.string.nav_settings));
                        SettingFragment settingFragment = new SettingFragment();
                        ft = fm.beginTransaction();
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.frag_container, settingFragment);
                        ft.commit();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.d("MADAPP", "Open=" + open);
        switch(open) {
            case 0:
                navigation.setSelectedItemId(R.id.nav_orders);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.nav_settings);
                break;
            default:
                navigation.setSelectedItemId(R.id.nav_orders);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void openOrder(Order order) {
        Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
