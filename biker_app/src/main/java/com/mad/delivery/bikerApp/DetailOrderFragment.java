package com.mad.delivery.bikerApp;


import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOrderFragment extends Fragment {
    public static final String DETAIL_ORDER_FRAGMENT_TAG = "detail_order_fragment";
    View view;
    TextView arrived;
    TextView requested;
    TextView status;
    TextView clientNotes;
    ImageView statusIcon;
    CardView cvAdminNotes;
    TextView serverNotes;
    List<Product> products;
    private RecyclerView recyclerView;
    public DetailOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        arrived = view.findViewById(R.id.detail_arrived);
        requested = view.findViewById(R.id.detail_requested);
        status = view.findViewById(R.id.detail_status);
        statusIcon = view.findViewById(R.id.status_icon);
        clientNotes = view.findViewById(R.id.client_notes_tv);
        Order order = getArguments().getParcelable("order");
        arrived.setText(MyDateFormat.parse(order.orderDate));
        requested.setText(MyDateFormat.parse(order.orderFor));
        status.setText(order.status.toString().toLowerCase());
        status.setTextColor(getColor(order.status));
        statusIcon.setColorFilter(getColor(order.status), PorterDuff.Mode.SRC_ATOP);
        clientNotes.setText(order.clientNotes);
        products = order.products;
        recyclerView = view.findViewById(R.id.rl_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductRecyclerViewAdapter(products));
        recyclerView.setNestedScrollingEnabled(false);
        cvAdminNotes = view.findViewById(R.id.cv_admin_notes_detail);
        serverNotes = view.findViewById(R.id.admin_notes_tv);
        if(order.serverNotes != null) {
            cvAdminNotes.setVisibility(View.VISIBLE);
            serverNotes.setText(order.serverNotes);
        } else {
            cvAdminNotes.setVisibility(View.GONE);
        }
        return view;
    }

    private int getColor(OrderStatus st) {
        switch(st) {
            case pending:
                return view.getResources().getColor(R.color.colorPendingOrder, null);
            case preparing:
                return view.getResources().getColor(R.color.colorPreparingOrder, null);
            case ready:
                return view.getResources().getColor(R.color.colorReadyOrder, null);
            case canceled:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
            case completed:
                return view.getResources().getColor(R.color.colorCompletedOrder, null);
            default:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
        }
    }


}
