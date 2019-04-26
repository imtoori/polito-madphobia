package com.mad.delivery.bikerApp.orders;


import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.delivery.bikerApp.Product;
import com.mad.delivery.bikerApp.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOrderFragment extends Fragment {
    public static final String DETAIL_ORDER_FRAGMENT_TAG = "detail_order_fragment";
    View view;

    TextView requested;
    TextView restAdd;
    TextView custAdd;
    TextView status;
    TextView clientNotes;
    TextView restNotes;
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
        requested = view.findViewById(R.id.detail_requested);
        status = view.findViewById(R.id.detail_status);
        statusIcon = view.findViewById(R.id.status_icon);
        clientNotes = view.findViewById(R.id.client_notes_tv);
        restNotes = view.findViewById(R.id.restaurant_notes_tv);
        restAdd=view.findViewById(R.id.detail_restaurantadd);
        custAdd=view.findViewById(R.id.detail_customeradd);
        Order order = getArguments().getParcelable("order");
        requested.setText(MyDateFormat.parse(order.orderFor));
        restAdd.setText(order.restaurant.road + " " + order.restaurant.houseNumber + ", " + order.restaurant.postCode + " " + order.restaurant.city + "(door " + order.restaurant.doorPhone+ ")");
        custAdd.setText(order.client.road + " " + order.client.houseNumber + ", " + order.client.postCode + " " + order.client.city + "(door " + order.client.doorPhone+ ")");
        status.setText(order.status.toString().toLowerCase());
        status.setTextColor(getColor(order.status));
        statusIcon.setColorFilter(getColor(order.status), PorterDuff.Mode.SRC_ATOP);
        clientNotes.setText(order.clientNotes);
        restNotes.setText(order.restNotes);
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
