package com.mad.delivery.bikerApp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.delivery.bikerApp.auth.OnLogin;
import com.mad.delivery.bikerApp.callBack.FirebaseCallback;
import com.mad.delivery.bikerApp.orders.OrderFragment;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Haversine;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.OnImageUploaded;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


final public class BikerDatabase {
    private static BikerDatabase instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;
    private final double REVENUE_FOR_SINGLE_RIDE = 2.5;
    DatabaseReference ordersRef;
    StorageReference storageRef;
    DatabaseReference myRef;

    FirebaseAuth mAuth;

    public static BikerDatabase getInstance() {
        if (instance == null) {
            instance = new BikerDatabase();
        }
        return instance;
    }

    private BikerDatabase() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        ordersRef = database.getReference("orders");
        storageRef = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference();
    }

    public void getOrderById(String id, FirebaseCallbackItem<Order> firebaseCallback) {
        myRef.child("orders").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.exists()));
                Order order = dataSnapshot.getValue(Order.class);
                if (order != null) {
                    order.id = id;
                    firebaseCallback.onCallback(order);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void checkLogin(String id, OnLogin<Biker> cb) {
        if (id == null || id.equals("")) {
            Log.d("MADAPP", "checkLogin: id null or empty");
            cb.onFailure();
            return;
        }
        myRef.child("users").child("biker").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Biker rest = dataSnapshot.getValue(Biker.class);
                    Log.d("MADAPP", "checkLogin: OK");
                    cb.onSuccess(rest);
                } else {
                    Log.d("MADAPP", "checkLogin: dataSnapshop doesn't exists.");

                    cb.onFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onFailure();
            }
        });
    }

    public void getBikerStatus(FirebaseCallbackItem<Boolean> firebaseCallback) {
        myRef.child("users").child("biker").child(mAuth.getUid()).child("profile").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean b = dataSnapshot.getValue(Boolean.class);
                    if (b != null) {
                        firebaseCallback.onCallback(b);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void reset() {
        instance = null;
    }

    void updateToken(String id, String token) {
        myRef.child("users").child("biker").child(id).child("token").setValue(token);
    }

    public void update(Order o) {
     /*   Order old = orders.get(o.id);
        if (old != null) {
            Log.d("MADAPP", "Order with ID " + o.id + " has been updated.");
            old.update(o);
        }*/
        o.bikerId = mAuth.getUid();
        if(o.status.equals(OrderStatus.delivered)) o.feedbackIsPossible = true;
        ordersRef.child(o.id).setValue(o);

    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }

    public void getPendingOrders(FirebaseCallback firebaseCallback) {
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> pendings = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if (o.status.equals(OrderStatus.preparing) || o.status.equals(OrderStatus.ready)) {
                            o.id = issue.getKey();
                            pendings.add(o);
                        }
                    }
                }
                Collections.sort(pendings, myDateComparator);
                firebaseCallback.onCallbak(pendings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void getPreparingOrders(FirebaseCallback firebaseCallback) {
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> preparing = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if (o.status.equals(OrderStatus.completed)) {
                            o.id = issue.getKey();
                            preparing.add(o);
                        }
                    }
                }
                Collections.sort(preparing, myDateComparator);
                firebaseCallback.onCallbak(preparing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCompletedOrders(FirebaseCallback firebaseCallback) {
        ordersRef.orderByChild("bikerId").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> completed = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);

                        if (o.status.equals(OrderStatus.delivered)) {
                            o.id = issue.getKey();
                            completed.add(o);
                        }
                    }
                }

                Collections.sort(completed, myDateComparator);
                firebaseCallback.onCallbak(completed);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getBikerProfile(String id, OnFirebaseData<Biker> cb) {
        myRef.child("users").child("biker").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Biker item = dataSnapshot.getValue(Biker.class);
                    if (item != null) {
                        cb.onReceived(item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setBikerStatus(String bikerID, Boolean status) {
        if (bikerID == null) return;
        myRef.child("users").child("biker").child(bikerID).child("status").setValue(status);
    }

    public void downloadImage(String id, String path, String imageName, OnImageDownloaded uriImage) {
        if (imageName == null || imageName.equals("")) {
            uriImage.onReceived(Uri.EMPTY);
            return;
        }
        StorageReference profileRefStore = storageRef.child("users").child("biker").child(id).child(path).child(imageName);
        profileRefStore.getDownloadUrl().addOnSuccessListener(uri -> {
            uriImage.onReceived(uri);
        }).addOnFailureListener(exception -> {
            exception.printStackTrace();
            uriImage.onReceived(Uri.EMPTY);
        });
    }

    public void uploadImage(String id, Uri uri, String path, String imageName, OnImageUploaded cb) {
        StorageReference profileRefStore = storageRef.child("users").child("biker").child(id).child(path).child(imageName);
        profileRefStore.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Log.d("MADAPP", "The image profile has been uploaded.");
            cb.onFinished();
        });
    }

    public void updateBikerProfile(Biker r, Uri image) {
        if (image == null || image.toString().equals("") || image.toString().contains("https:")) {
            myRef.child("users").child("biker").child(r.id).setValue(r);
            return;
        }

        uploadImage(r.id, image, "profile", image.getLastPathSegment(), () -> {
            r.imageName = image.getLastPathSegment();
            myRef.child("users").child("biker").child(r.id).setValue(r);
        });
    }

    public void updateBikerVisibility(String bikerID, boolean value, OnFirebaseData<Boolean> cb) {
        myRef.child("users").child("biker").child(bikerID).child("visible").setValue(value, (databaseError, databaseReference) -> {
            cb.onReceived(value);
        });
    }

    public void getBikerPosition(OnFirebaseData<LatLng> latLng) {
        myRef.child("users").child("biker").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Biker item = dataSnapshot.getValue(Biker.class);
                    if (item != null) {
                        LatLng latLang = new LatLng(item.latitude, item.longitude);
                        latLng.onReceived(latLang);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setBikerPosition(Double lat, Double lon) {
        myRef.child("users").child("biker").child(mAuth.getUid()).child("latitude").setValue(lat);
        myRef.child("users").child("biker").child(mAuth.getUid()).child("longitude").setValue(lon);
    }

    public void getDistanceRide(FirebaseCallbackItem<Double> firebaseCallbackItem){
        myRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double distance =0.0;
                if(dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if(snapshot.getValue(Order.class).bikerId!=null&&snapshot.getValue(Order.class).distanceRide!=null) {
                            if (snapshot.getValue(Order.class).bikerId.matches(mAuth.getUid())) {
                                distance += snapshot.getValue(Order.class).distanceRide;
                            }
                        }
                    }
                }
                firebaseCallbackItem.onCallback(distance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getCashBiker(FirebaseCallbackItem<Double> firebaseCallbackItem){
        myRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double cash =0.0;
                if(dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if(snapshot.getValue(Order.class).bikerId!=null) {
                            if (snapshot.getValue(Order.class).bikerId.matches(mAuth.getUid())) {
                                cash += REVENUE_FOR_SINGLE_RIDE;
                            }
                        }
                    }
                }
                firebaseCallbackItem.onCallback(cash);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getOrdersTaken(FirebaseCallbackItem<Integer> firebaseCallback){
        myRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value =0;
                if(dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if(snapshot.getValue(Order.class).bikerId!=null) {
                            if (snapshot.getValue(Order.class).bikerId.matches(mAuth.getUid())) {
                                value++;
                            }
                        }
                    }
                }
                firebaseCallback.onCallback(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


