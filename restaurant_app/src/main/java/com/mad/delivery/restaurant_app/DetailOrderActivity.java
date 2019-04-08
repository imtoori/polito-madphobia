package com.mad.delivery.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class DetailOrderActivity extends AppCompatActivity {
    Toolbar myToolBar;
    FragmentManager fm = getSupportFragmentManager();
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);

        Bundle bundle = getIntent().getExtras();
        Order order  = bundle.getParcelable("order");
        setTitle("Order Detail " + order.id);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.detail_order_pager);
        pagerAdapter = new DetailOrderPageAdapter(getSupportFragmentManager(), this, order);
        mPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.detail_header);
        tabLayout.setupWithViewPager(mPager);
    }
}
