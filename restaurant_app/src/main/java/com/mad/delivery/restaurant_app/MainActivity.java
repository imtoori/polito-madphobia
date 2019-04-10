package com.mad.delivery.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements PendingOrdersFragment.OnPendingOrderListener, SettingFragment.OnSettingListener {
    Toolbar myToolBar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft;
            switch (item.getItemId()) {
                case R.id.nav_menu:
                    setTitle(getString(R.string.nav_menu));
                    MenuFragment menuFragment = new MenuFragment();
                    ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.replace(R.id.frag_container, menuFragment);
                    ft.commit();
                    return true;
                case R.id.nav_orders:
                    setTitle(getString(R.string.nav_orders));
                    OrderFragment orderFragment = new OrderFragment();
                    ft = fm.beginTransaction();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.app_name));
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.nav_orders);


    }

    @Override
    public void openOrder(Order order) {
        Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void openProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
