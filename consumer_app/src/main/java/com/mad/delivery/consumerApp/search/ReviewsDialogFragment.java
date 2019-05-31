package com.mad.delivery.consumerApp.search;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.DistanceBiker;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.SortByClosestDistance;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ReviewsDialogFragment extends DialogFragment {
    private String restID;
    private Context context;
    private ImageView btnClose;
    private RecyclerView recyclerView;
    private List<Feedback> reviews;
    private ReviewsAdapter reviewsAdapter;

    public static ReviewsDialogFragment newInstance(String param) {
        ReviewsDialogFragment mapFragment = new ReviewsDialogFragment();
        Bundle args = new Bundle();
        args.putString("restaurantID", param);
        mapFragment.setArguments(args);
        return mapFragment;
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
        View rootView = inflater.inflate(R.layout.fragment_review_dialog, container, false);
        try {
            this.restID = (String) getArguments().get("restaurantID");
        } catch(NullPointerException e) {
            dismiss();
        }
        btnClose = rootView.findViewById(R.id.img_btn_close);
        recyclerView = rootView.findViewById(R.id.rv_reviews);
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(reviewsAdapter);
        btnClose.setOnClickListener(v -> {
            dismiss();
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConsumerDatabase.getInstance().getReviews(restID, r -> {
            reviews.add(r);
            reviewsAdapter.notifyDataSetChanged();
        });
    }
}