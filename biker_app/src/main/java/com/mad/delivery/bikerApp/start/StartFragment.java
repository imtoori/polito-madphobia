package com.mad.delivery.bikerApp.start;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mad.delivery.bikerApp.Database;
import com.mad.delivery.bikerApp.FirebaseCallbackItem;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.callBack.FirebaseCallback;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Order;

import java.util.List;

import androidx.fragment.app.Fragment;


public class StartFragment extends Fragment {
    public static final String SETTING_FRAGMENT_TAG = "statistics_fragment";

    private TextView ordersTaken, earning, hours, kilometers;
    private int n=0;
    private Button status;
    private boolean working = false;
    private double singleImport=2.50, kilometer=0.00;

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

        Database.getInstance().getBikerStatus(new FirebaseCallbackItem<Boolean>() {
            @Override
            public void onCallback(Boolean Item) {
                Log.i("STAT","status:"+Item);
                if(Item)
                    status.setText(R.string.stop_work);
                else
                    status.setText(R.string.start_work);


            }
        });
        n=0;
        Database.getInstance().getPreparingOrders(new FirebaseCallback() {
            @Override
            public void onCallbak(List<Order> list) {
                n+=list.size();
                }
            });
        Database.getInstance().getCompletedOrders(new FirebaseCallback() {
            @Override
            public void onCallbak(List<Order> list) {
                n+=list.size();
            }
        });
        Database.getInstance().getPendingOrders(new FirebaseCallback() {
            @Override
            public void onCallbak(List<Order> list) {
                n+=list.size();
            }
        });
        ordersTaken.setText(String.valueOf(n));
        earning.setText(String.valueOf(n*singleImport)+"€");
        kilometers.setText(String.valueOf(kilometer));
        hours.setText("0.00");

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("STAT","on click->"+working);
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
