package com.mad.delivery.restaurant_app;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTimeComparator;

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

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //TODO: after login implementation use current user
        restaurantRef = database.getReference("users/restaurants/demoRestaurant");
        menuItemsRef = database.getReference("users/restaurants/demoRestaurant/menuItems");
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

    public void addMenuItems(String name, String description, String category, String price, String availability, String time, String imgUri, Uri url, List<String> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, url, subItems);
        menuItemsRef.push().setValue(item);
        //TODO: add callback may be useful
    }


    public static List<Order> getPendingOrders() {
        List<Order> pendings = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders() size=" + pendings.size());
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


    public void setMenuItems(String id, String name, String category, String description, String price, String availability, String time, String imgUri, Uri url, List<String> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, url, subItems);
        menuItemsRef.child(id).setValue(item);
        //TODO add callback
    }


    void getMenuItems(OnDataFetched<List<MenuItemRest>, String> onDataFetched) {
        menuItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public MenuItemRest getMenuItem(String id, OnDataFetched<MenuItemRest, String> onDataFetched) {
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

