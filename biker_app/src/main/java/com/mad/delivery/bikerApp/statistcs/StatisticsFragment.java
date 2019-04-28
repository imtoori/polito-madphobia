package com.mad.delivery.bikerApp.statistcs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.settings.ProfileActivity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class StatisticsFragment extends Fragment {
    public static final String SETTING_FRAGMENT_TAG = "statistics_fragment";

    private TextView orders, earning;
    private Switch status;

    public StatisticsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        orders = view.findViewById(R.id.tv_statistics_orders);
        earning = view.findViewById(R.id.tv_statistics_earnings);
        status= view.findViewById(R.id.switch_status);
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //change status in DB->on

                }
                else{
                    //change status in DB->off
                }


            }
        });


        return view;
    }




}
