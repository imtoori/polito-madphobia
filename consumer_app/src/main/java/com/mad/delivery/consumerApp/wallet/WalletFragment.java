package com.mad.delivery.consumerApp.wallet;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.search.CategoriesFragment;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    private WalletFragment.OnOrderSelected mListener;
    public WalletFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletFragment.OnOrderSelected) {
            mListener = (WalletFragment.OnOrderSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRestaurantSelected");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // TODO usare metodo checkCreditCode per verificare un codice di credito
       /* ConsumerDatabase.getInstance().checkCreditCode("TO10", new ConsumerDatabase.firebaseCallback<CreditCode>() {
            @Override
            public void onCallBack(CreditCode item) {
            }
        });
        */
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = v.findViewById(R.id.orders_rv);
        List<Order> orders= new ArrayList<>();
        Log.d("MAD","Sono nel wallet!");
       // ConsumerDatabase.getInstance().updateCreditCustomer(20);
        // TODO remove items here when persistence is implemented
        //TODO usare metodo getCompletedOrders per farsi restituire gli ordini completati del cliente
        List<Product> products = new ArrayList<>();
        Product p1 = new Product("Prodotto 1", 20, 18.55);
        Product p2 = new Product("Prodotto 2", 10, 17.01);
        products.add(p1);
        products.add(p2);
        Restaurant rest= new Restaurant();
        rest.name = "Pinco";
        rest.description = "This is a description";
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy HH:mm");
        DateTime from = new DateTime(2019, 3, 1, 19, 20, 30);
        DateTime to = DateTime.now();
        Order o = new Order(new User(),rest,products, from.toString(), "credit");
        o.id = "1234";
        o.status = OrderStatus.pending;
        o.orderDate = to.toString();
        o.estimatedDelivery = to.toString();
        o.clientNotes = "Notes added by client";
        o.serverNotes="Notes added by restaurant";

        orders.add(o);
        orders.add(o);
        orders.add(o);
        orders.add(o);
        orders.add(o);
        orders.add(o);

        OrdersAdapter ordersAdapter = new OrdersAdapter(orders, mListener);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ordersAdapter);

        return v;


    }

    public interface OnOrderSelected {
        void openOrder();
    }

}
