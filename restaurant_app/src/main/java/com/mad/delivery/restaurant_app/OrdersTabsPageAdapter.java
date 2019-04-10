package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                PendingOrdersFragment pendingFragment = new PendingOrdersFragment();

                return pendingFragment;
            case 1:
                PreparingOrdersFragment preparingFragment = new PreparingOrdersFragment();

                return preparingFragment;
            case 2:
                CompletedOrdersFragment completedFragment = new CompletedOrdersFragment();

                return completedFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 0:
                return context.getString(R.string.pending_orders);
            case 1:
                return context.getString(R.string.preparing_orders);
            case 2:
                return context.getString(R.string.completed_orders);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
