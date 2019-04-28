package com.mad.delivery.consumerApp.search;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mad.delivery.consumerApp.R;

public class RestaurantInfoActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = findViewById(R.id.restaurant_pager);
        pagerAdapter = new RestaurantInfoPageAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        tabLayout = findViewById(R.id.tab_restaurant_header);
        tabLayout.setupWithViewPager(mPager);
    }


}
