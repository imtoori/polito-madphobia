package com.mad.delivery.restaurant_app.orders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.R;

public class OrdersTabsPageAdapter extends FragmentPagerAdapter {
    private Context context;
    private PendingOrdersFragment pendingFragment = new PendingOrdersFragment();
    private PreparingOrdersFragment preparingFragment = new PreparingOrdersFragment();
    private CompletedOrdersFragment completedFragment = new CompletedOrdersFragment();
    private Restaurant restaurant;

    public OrdersTabsPageAdapter(FragmentManager fm, Context context, Restaurant restaurant) {
        super(fm);
        this.context = context;
        this.restaurant = restaurant;
        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant", restaurant);
        pendingFragment.setArguments(bundle);
        preparingFragment.setArguments(bundle);
        completedFragment.setArguments(bundle);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return pendingFragment;
            case 1:
                return preparingFragment;
            case 2:
                return completedFragment;
            default:
                return pendingFragment;
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
