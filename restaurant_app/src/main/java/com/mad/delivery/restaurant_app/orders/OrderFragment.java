package com.mad.delivery.restaurant_app.orders;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mad.delivery.restaurant_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    public static final String ORDER_FRAGMENT_TAG = "order_fragment";
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    public OrderFragment() {
        // Required empty public constructor
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflator = inflater.inflate(R.layout.fragment_order, container, false);
        return inflator;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   setHasOptionsMenu(false);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = view.findViewById(R.id.orders_pager);

        pagerAdapter = new OrdersTabsPageAdapter(getChildFragmentManager(), getContext());
        mPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.tab_header);
        tabLayout.setupWithViewPager(mPager);
    }


}
