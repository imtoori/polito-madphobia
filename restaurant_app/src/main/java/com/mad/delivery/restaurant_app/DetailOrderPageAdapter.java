package com.mad.delivery.restaurant_app;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DetailOrderPageAdapter extends FragmentPagerAdapter {
    private Context context;
    private Order order;
    public DetailOrderPageAdapter(FragmentManager fm, Context context, Order order) {
        super(fm);
        this.context = context;
        this.order = order;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 1:
                UserInformationFragment uiFrag = new UserInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("client", order.client);
                uiFrag.setArguments(bundle);
                return uiFrag;
            case 0:
                DetailOrderFragment doFrag = new DetailOrderFragment();
                Bundle doBundle = new Bundle();
                doBundle.putParcelable("order", order);
                doFrag.setArguments(doBundle);
                return doFrag;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 1:
                return context.getString(R.string.user_info_order);
            case 0:
                return context.getString(R.string.detail_order_info);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

