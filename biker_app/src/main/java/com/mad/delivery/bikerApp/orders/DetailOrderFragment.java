package com.mad.delivery.bikerApp.orders;


import android.graphics.PorterDuff;
import android.graphics.Typeface;
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

import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.Product;

import org.joda.time.DateTime;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailOrderFragment extends Fragment {
    public static final String DETAIL_ORDER_FRAGMENT_TAG = "detail_order_fragment";
    View view;

    TextView orderSent;
    TextView requested;
    TextView status;
    TextView totalPrice;
    TextView paymentMethod;
    TextView clientNotes;
    TextView restNotes;
    ImageView statusIcon;
    List<Product> products;
    CardView cvClientNotes, cvRestaurantNotes;
    Order order;
    int price;
    private RecyclerView recyclerView;
    public DetailOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        orderSent = view.findViewById(R.id.order_sent);
        requested = view.findViewById(R.id.order_requested);
        status = view.findViewById(R.id.detail_status);
        statusIcon = view.findViewById(R.id.status_icon);
        clientNotes = view.findViewById(R.id.client_notes_tv);
        cvClientNotes = view.findViewById(R.id.cv_client_notes);
        cvRestaurantNotes = view.findViewById(R.id.cv_restaurant_notes);
        restNotes = view.findViewById(R.id.restaurant_notes_tv);
        paymentMethod = view.findViewById(R.id.order_payment_method);
        totalPrice = view.findViewById(R.id.order_price);
        order = (Order) getArguments().get("order");

        orderSent.setText(MyDateFormat.parse(new DateTime(order.orderDate)));
        requested.setText(MyDateFormat.parse(new DateTime(order.orderFor)));

        status.setText(order.status.toString().toLowerCase());
        status.setTextColor(getColor(order.status));
        statusIcon.setColorFilter(getColor(order.status), PorterDuff.Mode.SRC_ATOP);
        price = 0;
        order.products.stream().forEach(p -> price += p.price);
        totalPrice.setText("€ "+ price);
        paymentMethod.setText(order.paymentMethod);

        if(order.clientNotes == null || order.clientNotes.equals("")) {
            cvClientNotes.setVisibility(View.GONE);
        } else {
            clientNotes.setText(order.clientNotes);
            cvClientNotes.setVisibility(View.VISIBLE);
        }

        if(order.serverNotes == null || order.serverNotes.equals("")) {
            cvRestaurantNotes.setVisibility(View.GONE);
        } else {
            restNotes.setText(order.serverNotes);
            cvRestaurantNotes.setVisibility(View.VISIBLE);
        }

        products = order.products;
        recyclerView = view.findViewById(R.id.rl_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductRecyclerViewAdapter(products));
        recyclerView.setNestedScrollingEnabled(false);

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
            case delivered:
                return view.getResources().getColor(R.color.colorDeliveredOrder, null);
            default:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
        }
    }


}
