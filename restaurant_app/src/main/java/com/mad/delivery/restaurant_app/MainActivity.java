package com.mad.delivery.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;
import com.mad.delivery.restaurant_app.menu.MenuFragment;
import com.mad.delivery.restaurant_app.orders.DetailOrderActivity;
import com.mad.delivery.restaurant_app.orders.OrderFragment;
import com.mad.delivery.restaurant_app.orders.PendingOrdersFragment;
import com.mad.delivery.restaurant_app.settings.SettingFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity implements PendingOrdersFragment.OnPendingOrderListener {
    Toolbar myToolBar;
    private Order orderToBeUpdated;
    int open = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Restaurant restaurant;

    MenuFragment menuFragment = new MenuFragment();
    OrderFragment orderFragment = new OrderFragment();
    SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        if (getIntent().hasExtra("open")) {
            open = getIntent().getIntExtra("open", 1);
        } else {
            Log.d("MADAPP", "NO open..");
        }

        if (getIntent().hasExtra("orderToBeUpdated")) {
            Bundle bundle = getIntent().getExtras();
            orderToBeUpdated = bundle.getParcelable("orderToBeUpdated");
        }
        myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.app_name));
        BottomNavigationView navigation = findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener = item -> {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft;
            switch (item.getItemId()) {
                case R.id.nav_menu:
                    open = 0;
                    setTitle(getString(R.string.nav_menu));
                    ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.frag_container, menuFragment);
                    ft.commit();
                    return true;
                case R.id.nav_orders:
                    open = 1;
                    setTitle(getString(R.string.nav_orders));
                    ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    if (orderToBeUpdated != null)
                        bundle.putParcelable("orderToBeUpdated", orderToBeUpdated);
                    orderFragment.setArguments(bundle);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.frag_container, orderFragment);
                    ft.commit();
                    return true;
                case R.id.nav_settings:
                    open = 2;
                    setTitle(getString(R.string.nav_settings));
                    ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.frag_container, settingFragment);
                    ft.commit();
                    return true;
            }
            return false;
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switch (open) {
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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
                @Override
                public void onSuccess(Restaurant user) {
                    Log.d("MADAPP", "User "+ user.previewInfo.id + " have logged in.");
                    restaurant = user;
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> RestaurantDatabase.getInstance().updateToken(user.previewInfo.id, instanceIdResult.getToken()));
                    FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getUid() + ".order.new");
                }

                @Override
                public void onFailure() {
                    Log.d("MADAPP", "User "+ currentUser.getUid() + " can't log in.");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

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
                /*Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
