package com.mad.delivery.consumerApp.search;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.delivery.consumerApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantDescriptionFragment extends Fragment {


    public RestaurantDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("MADAPP", "Restaurant fragment created");
        View view = inflater.inflate(R.layout.fragment_restaurant_description, container, false);
        return view;
    }

}
