package com.mad.delivery.restaurant_app.orders;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.mad.delivery.resources.Order;
import com.mad.delivery.restaurant_app.MapBikersViewFragment;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.UserInformationFragment;

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
            case 0:
                DetailOrderFragment doFrag = new DetailOrderFragment();
                Bundle doBundle = new Bundle();
                doBundle.putParcelable("order", order);
                doFrag.setArguments(doBundle);
                return doFrag;
            case 1:
                UserInformationFragment uiFrag = new UserInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("client", order.client);
                uiFrag.setArguments(bundle);
                return uiFrag;
            case 2:
                MapBikersViewFragment mapFrag = new MapBikersViewFragment();
                Bundle bnd = new Bundle();
                LatLng r = new LatLng(order.restaurant.latitude,order.restaurant.longitude);
                bnd.putParcelable("restaurant", r);
                mapFrag.setArguments(bnd);
                return mapFrag;


            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch(position) {
            case 0:
                return context.getString(R.string.detail_order_info);
            case 1:
                return context.getString(R.string.user_info_order);
            case 2:
                return context.getString(R.string.user_map_info);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

