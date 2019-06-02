package com.mad.delivery.restaurant_app.orders;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.OnFirebaseData;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    public static final String ORDER_FRAGMENT_TAG = "order_fragment";
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private Restaurant restaurant;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    public OrderFragment() {
        // Required empty public constructor
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflator = inflater.inflate(R.layout.fragment_order, container, false);
        restaurant = (Restaurant) getArguments().get("restaurant");
        return inflator;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   setHasOptionsMenu(false);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = view.findViewById(R.id.orders_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_header);
        tabLayout.setupWithViewPager(mPager);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }


        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant user) {
                Log.d("MADAPP", "User "+ user.previewInfo.id + " have logged in.");
                restaurant = user;
                pagerAdapter = new OrdersTabsPageAdapter(getChildFragmentManager(), getContext(), restaurant);
                mPager.setAdapter(pagerAdapter);
            }

            @Override
            public void onFailure() {
                Log.d("MADAPP", "User "+ currentUser.getUid() + " can't log in.");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



    }


}
