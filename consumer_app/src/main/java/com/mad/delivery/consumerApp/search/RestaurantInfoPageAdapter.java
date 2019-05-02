package com.mad.delivery.consumerApp.search;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mad.delivery.consumerApp.R;

public class RestaurantInfoPageAdapter extends FragmentPagerAdapter {
    private Context context;

    public RestaurantInfoPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new RestaurantDescriptionFragment();
            case 1:
                return new RestaurantMenuFragment();
            default:
                return new RestaurantMenuFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 0:
                return context.getString(R.string.restaurant_info);
            case 1:
                return context.getString(R.string.restaurant_menu);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
