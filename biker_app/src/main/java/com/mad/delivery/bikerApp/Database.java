package com.mad.delivery.bikerApp;

import android.net.Uri;
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


final class Database {
    private static Database instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;


    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        myDateComparator = new MyDateComparator();
        orders = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            List<Product> products = new ArrayList<>();
            // create products for an order
            Product p1 = new Product("Prodotto 1", 20);
            Product p2 = new Product("Prodotto 2", 10);
            products.add(p1);
            products.add(p2);

            Customer u = new Customer();
            u.name = "FirstName";
            u.phoneNumber = "+393926282813";
            u.description = "This is a description";
            u.lastName = "Last Name";
            u.email = "email@email.it";
            u.city = "Turin";
            u.road = "Street name";
            u.houseNumber = "20";
            u.postCode = "10126";
            u.doorPhone = "doorphone";
            Restaurant rest= new Restaurant();
            rest.name = "Pinco";
            rest.phoneNumber = "+393926282813";
            rest.description = "This is a description";
            rest.email = "email@email.it";
            rest.city = "Turin";
            rest.road = "Street name";
            rest.houseNumber = "20";
            rest.postCode = "10126";
            rest.doorPhone = "pinco";

            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy HH:mm");
            DateTime from = new DateTime(2019, 3, 1, 19, 20, 30);
            DateTime to = DateTime.now();
            Order o = new Order(u,rest, products, from);
            o.id = "1234";
            o.status = OrderStatus.pending;
            o.orderDate = to;
            o.estimatedDelivery = to;
            o.clientNotes = "Notes added by client";
            o.restNotes="Notes added by restaurant";
            orders.put(o.id, o);


        }
    }

    public static void update(Order o) {
        Order old = orders.get(o.id);
        if (old != null) {
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
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders() size=" + pendings.size());
        return pendings;
    }

    public static List<Order> getPreparingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> preparing = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.preparing) || o.status.equals(OrderStatus.ready))
                preparing.add(o);
        }
        Collections.sort(preparing, myDateComparator);
        Log.d("MADAPP", "requested getPreparingOrders() ");
        return preparing;
    }

    public static List<Order> getCompletedOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> completed = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.completed) || o.status.equals(OrderStatus.canceled))
                completed.add(o);
        }
        Collections.sort(completed, myDateComparator);
        Log.d("MADAPP", "requested getCompletedOrders(), size is " + completed.size());
        return completed;
    }



}