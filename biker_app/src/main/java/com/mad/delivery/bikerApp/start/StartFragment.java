package com.mad.delivery.bikerApp.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mad.delivery.bikerApp.Database;
import com.mad.delivery.bikerApp.R;

import androidx.fragment.app.Fragment;


public class StartFragment extends Fragment {
    public static final String SETTING_FRAGMENT_TAG = "statistics_fragment";

    private TextView ordersTaken, earning, hours, kilometers;
    private Button status;
    private boolean working = false;

    public StartFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ordersTaken = view.findViewById(R.id.orders_taken);
        earning = view.findViewById(R.id.earnings);
        hours= view.findViewById(R.id.hours);
        kilometers = view.findViewById(R.id.kilometers);
        status = view.findViewById(R.id.status);
        ordersTaken.setEnabled(working);
        earning.setEnabled(working);
        hours.setEnabled(working);
        kilometers.setEnabled(working);
        status.setText(R.string.start_work);
        Database.getInstance().setBikerStatus(working);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                working = !working;
                ordersTaken.setEnabled(working);
                earning.setEnabled(working);
                hours.setEnabled(working);
                kilometers.setEnabled(working);
                if(working) {
                    status.setText(R.string.start_work);
                    Database.getInstance().setBikerStatus(!working);
                } else {
                    status.setText(R.string.stop_work);
                    Database.getInstance().setBikerStatus(!working);

                }
            }
        });


        return view;
    }




}
