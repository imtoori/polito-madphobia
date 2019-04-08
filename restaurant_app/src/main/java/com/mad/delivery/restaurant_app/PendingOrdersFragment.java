package com.mad.delivery.restaurant_app;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrdersFragment extends Fragment {
    public static final String PENDING_ORDERS_FRAGMENT_TAG = "pending_orders_fragment";

    private OnPendingOrderListener mListener;
    private List<Order> orders;
    private RecyclerView recyclerView;
    public PendingOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        recyclerView = view.findViewById(R.id.rl_pending);
        // TODO remove items here when persistence is implemented
        orders = Database.getPendingOrders();
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order first, Order second) {
                return second.orderFor.compareTo(first.orderFor);
            }
        });

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyOrderRecyclerViewAdapter(orders, mListener));

        return view;
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
}
