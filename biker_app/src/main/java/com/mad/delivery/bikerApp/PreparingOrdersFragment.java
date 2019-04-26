package com.mad.delivery.bikerApp;


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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreparingOrdersFragment extends Fragment {
    public static final String PREPARING_ORDERS_FRAGMENT_TAG = "preparing_orders_fragment";
    private PendingOrdersFragment.OnPendingOrderListener mListener;
    private RecyclerView recyclerView;
    private ImageView noOrderImg;
    private TextView noOrderTv;
    List<Order> orders;
    MyOrderRecyclerViewAdapter ordersAdapter;
    public PreparingOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PendingOrdersFragment.OnPendingOrderListener) {
            mListener = (PendingOrdersFragment.OnPendingOrderListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPendingOrderListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preparing_orders, container, false);
        recyclerView = view.findViewById(R.id.rl_preparing);
        noOrderImg = view.findViewById(R.id.img_no_completed_orders);
        noOrderTv = view.findViewById(R.id.tv_no_completed_orders);
        Log.d("MADAPP", "Preparing: onCreateView called");
        orders = new ArrayList<>();
        showEmptyFolder();
        Log.d("MADAPP", "Preparing: orders size = " + orders.size());
        ordersAdapter = new MyOrderRecyclerViewAdapter(orders, mListener);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ordersAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orders.addAll(Database.getPreparingOrders());
        recyclerView.getAdapter().notifyDataSetChanged();
        showEmptyFolder();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showEmptyFolder() {
        if(orders.size() == 0) {
            noOrderImg.setVisibility(View.VISIBLE);
            noOrderTv.setVisibility(View.VISIBLE);
        } else {
            noOrderImg.setVisibility(View.GONE);
            noOrderTv.setVisibility(View.GONE);
        }
    }
}
