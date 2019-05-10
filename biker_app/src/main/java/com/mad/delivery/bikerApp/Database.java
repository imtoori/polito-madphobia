package com.mad.delivery.bikerApp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.delivery.bikerApp.callBack.FirebaseCallback;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.Order;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
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
    StorageReference storageRef;

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
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        ordersRef = database.getReference("orders");
        profileRef = database.getReference("users/biker/"+mAuth.getUid());
        storageRef = FirebaseStorage.getInstance().getReference().child("users/biker/"+ mAuth.getUid());

    }

    public void reset() {
        instance = null;
    }

    void updateToken(String token) {
        Log.d("TOKEN", token);
        Database.getInstance().profileRef.child("token").setValue(token);
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
    public void getImage(String imageName,String path,FirebaseCallbackItem<Uri> UriImg) {

        if (imageName==null||!imageName.equals("")) {
            // Uri tmp = Uri.parse(imageName);
            StorageReference profileRefStore = storageRef.child(path + imageName);
            profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("DOWNLOAD",uri.toString());

                    UriImg.onCallback(uri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    UriImg.onCallback(Uri.EMPTY);
                }
            });
        }
    }

    public void putBikerProfile(Biker user){

        Uri file = Uri.parse(user.imageUri);
        storageRef.child("images/profile/");
        user.id= mAuth.getUid();
        StorageReference profileRefStore = storageRef.child("images/profile/" + user.imageName);
        profileRefStore.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        //Do what you want with the url
                        user.imageDownload =downloadUrl.toString();
                        profileRef.child("profile").setValue(user);

                    }
                });
            }
            });
     //   profileRef.setValue("profile");
        profileRef.child("profile").setValue(user);
       // user.imageDownload = storageRef.child("images/profile/" + user.imageName).getDownloadUrl().toString();





    }
    public void getBikerProfile(FirebaseCallbackItem<Biker> firebaseCallbackUser) {
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("profile")) {
                    dataSnapshot = dataSnapshot.child("profile");
                    Biker item = dataSnapshot.getValue(Biker.class);
                    if (item != null) {

                        firebaseCallbackUser.onCallback(item);
                    } else {
                        //   userStringOnDataFetched.onError("data not found");
                        Log.d("DATABASE: ", "SONO ENTRATO222");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "SONO ENTRATO");

            }
        });

    }

    public void setBikerStatus(Boolean status){
        profileRef.child("profile").child("status").setValue(status);
    }

}


