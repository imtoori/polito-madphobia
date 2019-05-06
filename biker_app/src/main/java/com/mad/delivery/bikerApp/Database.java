package com.mad.delivery.bikerApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.bikerApp.callBack.FirebaseCallback;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;

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


final public class Database {
    private static Database instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;
    DatabaseReference ordersRef;
    DatabaseReference profileRef;
    FirebaseAuth mAuth;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //TODO: call this after login
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        ordersRef = database.getReference("orders");
        profileRef = database.getReference("users/restaurants/"+mAuth.getUid());


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
            rest.scoreValue=10;

            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy HH:mm");
            DateTime from = new DateTime(2019, 3, 1, 19, 20, 30);
            DateTime to = DateTime.now();
            Order o = new Order(u,rest, products, from.toString());
            o.id = "1234";
            o.status = OrderStatus.pending;
            o.orderDate = to.toString();
            o.estimatedDelivery = to.toString();
            o.clientNotes = "Notes added by client";
            o.serverNotes ="Notes added by restaurant";
            o.bikerId = mAuth.getUid();
            ordersRef.push().setValue(o);
            orders.put(o.id, o);


        }
    }

    public  void update(Order o) {
     /*   Order old = orders.get(o.id);
        if (old != null) {
            Log.d("MADAPP", "Order with ID " + o.id + " has been updated.");
            old.update(o);
        }*/
        o.bikerId =mAuth.getUid();
        ordersRef.child(o.id).setValue(o);

    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }




    public  List<Order> getPendingOrders(FirebaseCallback firebaseCallback) {
     /*   if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> pendings = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders() size=" + pendings.size());
        return pendings;*/
        List<Order> pendings = new ArrayList<>();
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if(o.status.toString().equals("pending")) {
                            o.id = issue.getKey();
                            pendings.add(o);
                        }
                    }
                }
                firebaseCallback.onCallbak(pendings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


   /*     for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        */
        Collections.sort(pendings, myDateComparator);

        return pendings;
    }


    public  List<Order> getPreparingOrders(FirebaseCallback firebaseCallback) {
        List<Order> preparing = new ArrayList<>();
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if(o.status.toString().equals("preparing")) {
                            o.id = issue.getKey();
                            preparing.add(o);
                        }
                    }
                }
                firebaseCallback.onCallbak(preparing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return preparing;
    }

    public  List<Order> getCompletedOrders(FirebaseCallback firebaseCallback) {
        if (instance == null) {
            instance = new Database();
        }
        List<Order> completed = new ArrayList<>();
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);

                        if(o.status.toString().equals("completed")) {
                            o.id = issue.getKey();
                            completed.add(o);
                        }
                    }
                }
                firebaseCallback.onCallbak(completed);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return completed;
    }




}
