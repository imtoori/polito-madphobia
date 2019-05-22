package com.mad.delivery.bikerApp.orders;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.MapViewFragment;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.Order;

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
                UserInformationFragment uiFrag = new UserInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("client", order.client);
                uiFrag.setArguments(bundle);
                return uiFrag;
            case 1:
                DetailOrderFragment doFrag = new DetailOrderFragment();
                Bundle doBundle = new Bundle();
                doBundle.putParcelable("order", order);
                doFrag.setArguments(doBundle);
                return doFrag;
            case 2:
                RestaurantInformationFragment resFrag = new RestaurantInformationFragment();
                Bundle rbundle = new Bundle();
                rbundle.putParcelable("restaurant", order.restaurant);
                resFrag.setArguments(rbundle);
                return resFrag;
            case 3:
            MapViewFragment mapFrag = new MapViewFragment();
            Bundle bnd = new Bundle();
            LatLng latLngR = new LatLng(order.restaurant.latitude,order.restaurant.longitude);
            LatLng latLngC = new LatLng(order.latitude,order.longitude);
                bnd.putParcelable("restaurant", latLngR);
                bnd.putParcelable("client", latLngC);
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
                return context.getString(R.string.user_info_order);
            case 1:
                return context.getString(R.string.detail_order_info);
            case 2:
                return context.getString(R.string.detail_restaurant_info);
            case 3:
                return context.getString(R.string.user_map_info);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}

