package com.mad.delivery.consumerApp.search;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsFragment extends Fragment {
    final static public String RESTAURANT_FRAGMENT_TAG = "restfrag";
    private RecyclerView recyclerView;
    private RestaurantsAdapter restaurantAdapter;
    private RestaurantsFragment.OnRestaurantSelected mListener;
    private List<PreviewInfo> previews;
    private CardView emptyFolder;
    private boolean freeDelivery = false, minOrderCost = false;
    private Set<String> chosenCategories;
    private String address = "";
    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestaurantsFragment.OnRestaurantSelected) {
            mListener = (RestaurantsFragment.OnRestaurantSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRestaurantSelected");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        Log.d("MADAPP", "restaurantsFragment onCreateView");
        recyclerView = view.findViewById(R.id.restaurant_rv);
        emptyFolder = view.findViewById(R.id.emptyfolder_cv);
        previews = new ArrayList<>();
        restaurantAdapter = new RestaurantsAdapter(previews, mListener);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(restaurantAdapter);
        chosenCategories = new HashSet<>();

        List<String> categories = new ArrayList<>();

        try {
            categories = getArguments().getStringArrayList("categories");
            chosenCategories.addAll(categories);
            address = getArguments().getString("address");
            freeDelivery = getArguments().getBoolean("freeDelivery");
            minOrderCost = getArguments().getBoolean("minOrderCost");
        } catch(NullPointerException e) {
            // do nothing
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("MADAPP", "restaurantsFragment onCreatedView");
        ConsumerDatabase.getInstance().getRestaurants(chosenCategories, address, minOrderCost, freeDelivery, preview -> {
            previews.add(preview);
            restaurantAdapter.notifyDataSetChanged();
            if(previews.size() == 0) {
                // show empty viewcard
                emptyFolder.setVisibility(View.VISIBLE);
            } else {
                emptyFolder.setVisibility(View.GONE);
            }
        });

    }

    public interface OnRestaurantSelected {
        void openRestaurant(PreviewInfo previewInfo);

    }




}
