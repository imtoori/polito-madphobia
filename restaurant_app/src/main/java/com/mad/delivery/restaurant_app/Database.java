package com.mad.delivery.restaurant_app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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


final class Database {
    private static Database instance;
    private static Map<String, Order> orders = new HashMap<>();
    private static MyDateComparator myDateComparator;
    DatabaseReference restaurantRef;
    DatabaseReference menuItemsRef;
    DatabaseReference ordersRef;

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
        //TODO: after login implementation use current user
        restaurantRef = database.getReference("users/restaurants/demoRestaurant");
        menuItemsRef = database.getReference("users/restaurants/demoRestaurant/menuItems");
        ordersRef = database.getReference("orders");


    }



    void updateToken(String token) {
        Log.d("TOKEN", token);
        Database.getInstance().restaurantRef.child("token").setValue(token);
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

    public void addMenuItems(String name, String description, String category, String price, String availability, String time, String imgUri,  List<String> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, subItems);
        menuItemsRef.push().setValue(item);
        //TODO: add callback may be useful
    }


    public  List<Order> getPendingOrders(FirebaseCallback firebaseCallback) {
        List<Order> pendings = new ArrayList<>();
       ordersRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if(o!=null)
                            Log.d("ORDER","size"+pendings.size());
                            pendings.add(o);
                    }
                }
                firebaseCallback.onCallbak(pendings);
                Log.d("QQQQQQ", "requested getPendingOrders() size=" + pendings.size());
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

        Log.d("QQQQQQ", "requested getPendingOrders() size=" + pendings.size());
   return pendings;
    }

    public static List<Order> getPreparingOrders() {
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


    public void setMenuItems(String id, String name, String category, String description, String price, String availability, String time, String imgUri, List<String> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri,  subItems);
        menuItemsRef.child(id).setValue(item);
        //TODO add callback
    }


    void getMenuItems(OnDataFetched<List<MenuItemRest>, String> onDataFetched) {
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItemRest> menuItemRests = new ArrayList<>();
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    MenuItemRest item = snapshot.getValue(MenuItemRest.class);
                    if (item != null) {
                        item.id = snapshot.getKey();
                        menuItemRests.add(item);
                    } else {
                        onDataFetched.onError("data not found");
                    }
                }
                onDataFetched.onDataFetched(menuItemRests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onDataFetched.onError(databaseError.getMessage());
            }
        });
    }

    void removeMenuItem(MenuItemRest menuItemRest) {
        menuItemsRef.child(menuItemRest.id).removeValue();
        //TODO add callback
    }

    MenuItemRest getMenuItem(String id, OnDataFetched<MenuItemRest, String> onDataFetched) {
        menuItemsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MenuItemRest item = dataSnapshot.getValue(MenuItemRest.class);
                if (item != null) {
                    item.id = dataSnapshot.getKey();
                    onDataFetched.onDataFetched(item);
                } else {
                    onDataFetched.onError("data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onDataFetched.onError(databaseError.getMessage());
            }
        });
        return null;
    }
}

interface OnDataFetched<T, E> {
    void onDataFetched(T data);

    void onError(E error);
}

 interface FirebaseCallback{
    void  onCallbak(List<Order> list);
}
