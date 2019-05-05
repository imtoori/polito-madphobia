package com.mad.delivery.consumerApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.RestaurantInfoActivity;
import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.consumerApp.search.SearchFragment;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.RestaurantCategory;

public class HomeActivity extends AppCompatActivity implements RestaurantsFragment.OnRestaurantSelected, WalletFragment.OnOrderSelected {
    Toolbar myToolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    FragmentManager fm;
    FragmentTransaction ft;
    WalletFragment walletFragment;
    SearchFragment searchFragment;
    SettingsFragment settingsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myToolbar = findViewById(R.id.mainActivityToolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        setSupportActionBar(myToolbar);
        setTitle(getResources().getString(R.string.app_customer_name));
        fm = getSupportFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        walletFragment = new WalletFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();

        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu:
                        ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.frag_container, walletFragment);
                        ft.commit();
                        return true;
                    case R.id.nav_search:
                        ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.frag_container, searchFragment);
                        ft.commit();
                        return true;
                    case R.id.nav_settings:
                        ft = fm.beginTransaction();
                        //ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.frag_container, settingsFragment);
                        ft.commit();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.nav_search);
    }

    @Override
    public void onBackPressed() {
        Log.d("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("MADAPP", "onBackPressed and fragments count=" + fragments);
        if(searchFragment != null && searchFragment.getChildFragmentManager().getBackStackEntryCount() > 1) {
            searchFragment.getChildFragmentManager().popBackStack();
            return;
        }
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public void openRestaurant(PreviewInfo previewInfo) {
        Log.d("MADAPP", "Clicked on restaurant");
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra("restaurant", previewInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void openOrder() {
        Log.d("MADAPP", "Clicked on ORDER");
        Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_login:
                Log.d("MADAPP", "Opening Login Activity");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                // do nothing
                return super.onOptionsItemSelected(item);
        }
    }
}
