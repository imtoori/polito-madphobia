package com.mad.delivery.bikerApp.orders;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.callBack.FirebaseCallback;
import com.mad.delivery.resources.Order;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrdersFragment extends Fragment {
    public static final String PENDING_ORDERS_FRAGMENT_TAG = "pending_orders_fragment";

    private OnPendingOrderListener mListener;
    private RecyclerView recyclerView;
    private ImageView noOrderImg;
    private TextView noOrderTv;
    List<Order> orders;
    MyOrderRecyclerViewAdapter ordersAdapter;

    public PendingOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        recyclerView = view.findViewById(R.id.rl_pending);
        noOrderImg = view.findViewById(R.id.img_no_completed_orders);
        noOrderTv = view.findViewById(R.id.tv_no_completed_orders);
        orders = new ArrayList<>();
     //   showEmptyFolder();
        ordersAdapter = new MyOrderRecyclerViewAdapter(orders, mListener);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ordersAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BikerDatabase.getInstance().getPendingOrders( new FirebaseCallback() {
            @Override
            public void onCallbak(List<Order> list) {
                if(list.isEmpty())
                    showEmptyFolder();
                else {
                    orders.clear();
                    ordersAdapter.notifyDataSetChanged();
                    orders.addAll(list);
                    //ordersAdapter.orders = orders;
                    ordersAdapter.notifyDataSetChanged();
                }
            }
        });
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPendingOrderListener {
        void openOrder(Order order);
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
