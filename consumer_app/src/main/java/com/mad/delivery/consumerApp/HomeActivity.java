package com.mad.delivery.consumerApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.RestaurantInfoActivity;
import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.consumerApp.search.SearchFragment;
import com.mad.delivery.consumerApp.settings.SettingsFragment;
import com.mad.delivery.consumerApp.wallet.WalletFragment;
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
    int open = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 124);

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
        if(getIntent().hasExtra("open")) {
            open = getIntent().getIntExtra("open", 1);
        } else {
            Log.d("MADAPP", "NO open..");
        }
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu:
                        ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.replace(R.id.frag_container, walletFragment);
                        ft.commit();
                        return true;
                    case R.id.nav_search:
                        ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.replace(R.id.frag_container, searchFragment);
                        ft.commit();
                        return true;
                    case R.id.nav_settings:
                        ft = fm.beginTransaction();
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.frag_container, settingsFragment);
                        ft.commit();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switch(open) {
            case 0:
                navigation.setSelectedItemId(R.id.nav_menu);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.nav_search);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.nav_settings);
                break;
            default:
                navigation.setSelectedItemId(R.id.nav_search);
        }
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

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.

               // doLocationAccessRelatedJob();
            } else {
                // User refused to grant permission. You can add AlertDialog here
                Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                startInstalledAppDetailsActivity();
            }
        }
    }

    private void startInstalledAppDetailsActivity() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
