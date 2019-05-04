package com.mad.delivery.consumerApp.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;

public class RestaurantInfoPageAdapter extends FragmentPagerAdapter {
    private Context context;
    private Restaurant restaurant;
    RestaurantDescriptionFragment rdFragment;
    RestaurantMenuFragment rmFragment;
    Bundle bundle;
    public RestaurantInfoPageAdapter(FragmentManager fm, Context context, Restaurant restaurant) {
        super(fm);
        this.context = context;
        this.restaurant = restaurant;
        rdFragment = new RestaurantDescriptionFragment();
        rmFragment = new RestaurantMenuFragment();
        bundle = new Bundle();
        bundle.putParcelable("restaurant", this.restaurant);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                rdFragment.setArguments(bundle);
                return rdFragment;
            case 1:
                rmFragment.setArguments(bundle);
                return rmFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 0:
                return context.getString(R.string.restaurant_info);
            case 1:
                return context.getString(R.string.rest_menu);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
