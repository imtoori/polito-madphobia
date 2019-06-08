package com.mad.delivery.consumerApp.search;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.MenuCategory;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MenuOffer;
import com.mad.delivery.resources.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantMenuFragment extends Fragment {
    private MenuCategoriesAdapter mAdapter;
    private RecyclerView recyclerView;
    private Restaurant restaurant;
    private List<MenuCategory> categories;
    private TextView minOrder, deliveryCost;

    public RestaurantMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        minOrder = view.findViewById(R.id.rest_menu_minorder_cost);
        deliveryCost = view.findViewById(R.id.rest_menu_delivery_cost);

        restaurant = (Restaurant) getArguments().get("restaurant");
        categories = new ArrayList<>();
        Map<String, List<MenuItemRest>> menus = new HashMap<>();
        if (restaurant.menu != null) {
            restaurant.menu.values().stream().forEach(item -> {
                Log.d("MADAPP", "Item from restaurant = " + item.toString());
                if (item.availability > 0) {
                    menus.putIfAbsent(item.category, new ArrayList<>());
                    List<MenuItemRest> menuList = menus.get(item.category);
                    menuList.add(item);
                }
            });

            menus.entrySet().stream().forEach( entry -> {
                MenuCategory mc = new MenuCategory(entry.getKey(), entry.getValue());
                categories.add(mc);
            });
        }


        minOrder.setText(getResources().getString(R.string.min_order, String.valueOf(restaurant.previewInfo.minOrderCost)));
        deliveryCost.setText(getResources().getString(R.string.delivery_cost, String.valueOf(restaurant.previewInfo.deliveryCost)));
        mAdapter = new MenuCategoriesAdapter(categories, (MenuItemAdapter.OnItemSelected) getActivity());

        recyclerView = view.findViewById(R.id.restaurant_menu_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);


        return view;
    }

}
