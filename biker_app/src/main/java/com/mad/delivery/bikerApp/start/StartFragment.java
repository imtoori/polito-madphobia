package com.mad.delivery.bikerApp.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.FirebaseCallbackItem;
import com.mad.delivery.bikerApp.LocationTracker;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.auth.OnLogin;

import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Order;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class StartFragment extends Fragment {
    public static final String SETTING_FRAGMENT_TAG = "statistics_fragment";
    private TextView ordersTaken, earning, hours, kilometers;
    private Button status;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CardView cvStats;
    private TextView tvGreetings;
    private LinearLayout visibleFolder;
    private Biker biker;
    private DecimalFormat dec;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dec = new DecimalFormat("0.00");
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ordersTaken = view.findViewById(R.id.orders_taken);
        earning = view.findViewById(R.id.earnings);
        cvStats = view.findViewById(R.id.cardView3);
        hours= view.findViewById(R.id.hours);
        kilometers = view.findViewById(R.id.kilometers);
        tvGreetings = view.findViewById(R.id.tv_greets);
        visibleFolder = view.findViewById(R.id.ll_not_visible);
        status = view.findViewById(R.id.status);

        BikerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Biker>() {
            @Override
            public void onSuccess(Biker user) {
                biker = user;
                if(biker != null && biker.visible) {
                    status.setVisibility(View.VISIBLE);
                    cvStats.setVisibility(View.VISIBLE);
                    visibleFolder.setVisibility(View.GONE);
                    tvGreetings.setText("Hello, " + biker.name + "!");
                    tvGreetings.setVisibility(View.VISIBLE);
                    hours.setText(Math.round(biker.score) + "/5.0");
                    BikerDatabase.getInstance().getCashBiker(new FirebaseCallbackItem<Double>() {
                        @Override
                        public void onCallback(Double Item) {
                            earning.setText("€ " + dec.format(Item));
                        }
                    });
                    BikerDatabase.getInstance().getDistanceRide(new FirebaseCallbackItem<Double>() {
                        @Override
                        public void onCallback(Double Item) {
                            Double km = Item / 1000.0;
                            kilometers.setText(dec.format(km));
                        }
                    });
                    BikerDatabase.getInstance().getOrdersTaken(new FirebaseCallbackItem<Integer>() {
                        @Override
                        public void onCallback(Integer Item) {
                            ordersTaken.setText(Item.toString());
                        }
                    });
                } else {
                    status.setVisibility(View.GONE);
                    cvStats.setVisibility(View.GONE);
                    visibleFolder.setVisibility(View.VISIBLE);
                    tvGreetings.setVisibility(View.GONE);
                }

                //ordersTaken.setText(biker.order_count.toString());
                //earning.setText(biker.earning.toString());
                //kilometers.setText(biker.km.toString());
                //hours.setText(biker.hours.toString());
                setStatus(biker.status);
                status.setOnClickListener(v -> {
                    biker.status = !biker.status;
                    BikerDatabase.getInstance().setBikerStatus(biker.id, biker.status);
                    setStatus(biker.status);
                });
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void setStatus(boolean st) {
        if(st) {
            getActivity().startService(new Intent(getActivity(), LocationTracker.class));
            status.setText("Stop");
        } else {
            getActivity().stopService(new Intent(getActivity(), LocationTracker.class));
            status.setText("Start");

        }
    }




}
