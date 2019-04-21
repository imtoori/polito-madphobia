package com.mad.delivery.bikerApp;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public final class Database {
    private static Database instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;
    private Database() {
        myDateComparator = new MyDateComparator();
        int randForProducts;
        orders = new HashMap<>();

    }

    public static void update(Order o) {
        Order old = orders.get(o.id);
        if(old != null) {
            Log.d("MADAPP", "Order with ID " + o.id + " has been updated.");
            old.update(o);
        }
    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }


    public static List<Order> getPendingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> pendings = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders()");
        return pendings;
    }

    public static List<Order> getPreparingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> preparing = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.preparing) || o.status.equals(OrderStatus.ready)) preparing.add(o);
        }
        Collections.sort(preparing, myDateComparator);
        Log.d("MADAPP", "requested getPreparingOrders()");
        return preparing;
    }

    public static List<Order> getCompletedOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> completed = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.completed) || o.status.equals(OrderStatus.canceled)) completed.add(o);
        }
        Collections.sort(completed, myDateComparator);
        Log.d("MADAPP", "requested getCompletedOrders(), size is "  + completed.size());
        return completed;
    }

}

