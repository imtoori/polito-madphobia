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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.RestaurantInfoActivity;
import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.consumerApp.search.SearchFragment;
import com.mad.delivery.consumerApp.settings.ProfileActivity;
import com.mad.delivery.consumerApp.settings.SettingsFragment;
import com.mad.delivery.consumerApp.wallet.WalletFragment;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.PreviewInfo;

public class HomeActivity extends AppCompatActivity implements RestaurantsFragment.OnRestaurantSelected, WalletFragment.OnOrderSelected {
    Toolbar myToolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    FragmentManager fm;
    FragmentTransaction ft;
    WalletFragment walletFragment;
    SearchFragment searchFragment;
    SettingsFragment settingsFragment;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myRef = db.getReference();
        setContentView(R.layout.activity_home);
        myToolbar = findViewById(R.id.mainActivityToolbar);
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
        if (searchFragment != null && searchFragment.getChildFragmentManager().getBackStackEntryCount() > 1) {
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
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra("restaurant", previewInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void openOrder() {
        Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mUser == null) {
            getMenuInflater().inflate(R.menu.home_menu, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
