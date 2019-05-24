package com.mad.delivery.bikerApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.orders.DetailOrderActivity;
import com.mad.delivery.bikerApp.orders.OrderFragment;
import com.mad.delivery.bikerApp.orders.PendingOrdersFragment;
import com.mad.delivery.bikerApp.settings.SettingFragment;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Order;
import com.mad.delivery.bikerApp.start.StartFragment;

public class HomeActivity extends AppCompatActivity implements PendingOrdersFragment.OnPendingOrderListener {
    Toolbar myToolBar;
    private Order orderToBeUpdated;
    int open = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 124);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("open")) {
            open = getIntent().getIntExtra("open", 1);
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
                    case R.id.nav_statistics:
                        setTitle(getString(R.string.nav_statistics));
                        StartFragment statisticsFragment = new StartFragment();
                        ft = fm.beginTransaction();
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.frag_container, statisticsFragment);
                        ft.commit();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // todo: enable notifications!!
        //FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> BikerDatabase.getInstance().updateToken(mAuth.getCurrentUser().getUid(), instanceIdResult.getToken()));
        //FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getUid() + ".order.new");
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("MADAPP", "onSupportNavigateUp");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        return true;
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
        Log.d("ORDER: in openOrder1",order.toString());

        intent.putExtra("order", order);
        Log.d("ORDER: in openOrder2",order.toString());

        Log.d("MADAPP" , "order in HomeActivity: " + order.toString());
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
