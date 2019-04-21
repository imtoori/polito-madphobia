package com.mad.delivery.restaurant_app;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.jar.Attributes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity  implements PendingOrdersFragment.OnPendingOrderListener{
    Toolbar myToolBar;
    private Order orderToBeUpdated;
    int open = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                navigation.setSelectedItemId(R.id.nav_menu);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.nav_orders);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.nav_settings);
                break;
            default:
                navigation.setSelectedItemId(R.id.nav_menu);
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
            case R.id.menu_new_offert:

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
