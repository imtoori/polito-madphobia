package com.mad.delivery.consumerApp.search;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.Restaurant;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantDescriptionFragment extends Fragment {
    private Restaurant restaurant;
    private TextView description, openingHours, reviewCount, ratingNumber;
    private RatingBar ratingBar;
    private ReviewsDialogFragment dialogFragment;
    private CardView cvRating;

    public RestaurantDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_description, container, false);
        restaurant = (Restaurant) getArguments().get("restaurant");
        Log.i("MADAPP", restaurant.previewInfo.name);
        description = view.findViewById(R.id.rest_description_content);
        reviewCount = view.findViewById(R.id.reviews);
        ratingNumber = view.findViewById(R.id.tv_rating_number);
        ratingBar = view.findViewById(R.id.rateRestaurant);
        openingHours = view.findViewById(R.id.rest_opening_content);
        description.setText(restaurant.previewInfo.description);
        openingHours.setText(restaurant.openingHours);
        cvRating = view.findViewById(R.id.cv_rating);
        ratingBar.setRating(restaurant.previewInfo.scoreValue.floatValue());
        reviewCount.setText(restaurant.previewInfo.scoreCount + " reviews");
        ratingNumber.setText(restaurant.previewInfo.scoreValue + "/5.0");
        cvRating.setOnClickListener(v -> {
            dialogFragment = ReviewsDialogFragment.newInstance(restaurant.previewInfo.id);
            dialogFragment.show(getChildFragmentManager(), "reviewFragment");
        });


        return view;
    }

}
