package com.mad.delivery.consumerApp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.DistanceBiker;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.SortByClosestDistance;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

public class FeedBackFragment extends DialogFragment {
    private Order order;
    private ImageView btn_close;
    private Button saveFeedback;
    private RatingBar rateFood, rateRestaurant, rateBiker;
    private EditText etRestaurantComment, etBikerComment;
    private Feedback feedback;
    private Context context;

    public static FeedBackFragment newInstance(Order o) {
        FeedBackFragment frag = new FeedBackFragment();
        Bundle args = new Bundle();
        args.putParcelable("order", o);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        try {
            this.order = (Order) getArguments().get("order");
        } catch(NullPointerException e) {
            dismiss();
        }
        btn_close = rootView.findViewById(R.id.img_btn_close);
        saveFeedback = rootView.findViewById(R.id.btn_save_feedback);
        rateFood = rootView.findViewById(R.id.rate_food);
        rateBiker = rootView.findViewById(R.id.rate_biker);
        rateRestaurant = rootView.findViewById(R.id.rate_restaurant);
        etRestaurantComment = rootView.findViewById(R.id.et_feedback_restaurant);
        etBikerComment = rootView.findViewById(R.id.et_feedback_biker);

        btn_close.setOnClickListener(v -> {
            dismiss();
        });

            saveFeedback.setOnClickListener(v -> {
            feedback = new Feedback(order.clientId, order.restaurantId, order.id, (double) rateFood.getRating(),
                        (double) rateBiker.getRating(), (double) rateRestaurant.getRating());
            feedback.restaurantFeedback = etRestaurantComment.getText().toString();
            feedback.bikerFeedback = etBikerComment.getText().toString();
                ConsumerDatabase.getInstance().saveFeedback(order, feedback, value -> {
                    if(value)
                        Toast.makeText(context, "Your feedback has been saved!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "An error occurred while saving your feedback.", Toast.LENGTH_SHORT).show();
                });
            dismiss();
        });

        return rootView;
    }


}