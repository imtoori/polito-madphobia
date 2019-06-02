package com.mad.delivery.bikerApp.orders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mad.delivery.bikerApp.R;

public class OrdersTabsPageAdapter extends FragmentPagerAdapter {
    private Context context;

    public OrdersTabsPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new PendingOrdersFragment();
            case 1:
                return new PreparingOrdersFragment();
            case 2:
                return new CompletedOrdersFragment();
            default:
                return new PendingOrdersFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 0:
                return context.getString(R.string.waiting);
            case 1:
                return context.getString(R.string.assigned);
            case 2:
                return context.getString(R.string.delivered);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
