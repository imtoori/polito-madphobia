package com.mad.delivery.consumerApp.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.mad.delivery.consumerApp.Basket;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;

public class RestaurantInfoActivity extends AppCompatActivity {
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Restaurant restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        PreviewInfo previewInfo = new PreviewInfo();
        Toolbar toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = findViewById(R.id.restaurant_pager);
        tabLayout = findViewById(R.id.tab_restaurant_header);
        try {
            Bundle bundle = getIntent().getExtras();
            previewInfo = (PreviewInfo) bundle.get("restaurant");
            ConsumerDatabase.getInstance().getRestaurantInfo(previewInfo, (rest) -> {
                restaurant = rest;

                pagerAdapter = new RestaurantInfoPageAdapter(getSupportFragmentManager(), this, restaurant);
                mPager.setAdapter(pagerAdapter);
                collapsingToolbarLayout.setTitle(restaurant.name);
                // Give the TabLayout the ViewPager

                tabLayout.setupWithViewPager(mPager);
            });

        } catch(NullPointerException e) {
            // do nothing
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_info, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.option_shopping:
                Intent intent = new Intent(getApplicationContext(), Basket.class);
                startActivity(intent);
                // do nothing
                return true;
            default:
                // do nothing
                return super.onOptionsItemSelected(item);
        }
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
}
