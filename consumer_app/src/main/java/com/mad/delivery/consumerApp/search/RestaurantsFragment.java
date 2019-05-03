package com.mad.delivery.consumerApp.search;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView recyclerView;
    private RestaurantsAdapter restaurantAdapter;
    private RestaurantsFragment.OnRestaurantSelected mListener;
    private List<PreviewInfo> previews;

    private Set<String> chosenCategories;
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
        List<String> categories = new ArrayList<>();
        String address = new String();
        try {
            categories = getArguments().getStringArrayList("categories");
        } catch(NullPointerException e) {
            // do nothing
        }
        try {
            address = getArguments().getString("address");
        } catch(NullPointerException e) {
            // do nothing
        }
        chosenCategories = new HashSet<>();
        chosenCategories.addAll(categories);
        previews = new ArrayList<>();
        restaurantAdapter = new RestaurantsAdapter(previews, mListener);
        ConsumerDatabase.getInstance().getRestaurants(chosenCategories, address, preview -> {
            previews.add(preview);
            restaurantAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        recyclerView = view.findViewById(R.id.restaurant_rv);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(restaurantAdapter);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public interface OnRestaurantSelected {
        void openRestaurant(PreviewInfo previewInfo);
    }






}
