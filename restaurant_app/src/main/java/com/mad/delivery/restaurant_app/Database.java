package com.mad.delivery.restaurant_app;

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
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


final class Database {
    private static Database instance;
    private static MyDateComparator myDateComparator;
    DatabaseReference restaurantRef;
    DatabaseReference menuItemsRef;
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
        //TODO: call this after login
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        //TODO: after login implementation use current user
        restaurantRef = database.getReference("users/restaurants/" + mAuth.getUid());
        menuItemsRef = database.getReference("users/restaurants/"+mAuth.getUid()+"/menuItems");
        ordersRef = database.getReference("orders");
        profileRef = database.getReference("users/restaurants/"+mAuth.getUid());
        storageRef = FirebaseStorage.getInstance().getReference().child("users/"+ mAuth.getUid());


    }



    void updateToken(String token) {
        Log.d("TOKEN", token);
        Database.getInstance().restaurantRef.child("token").setValue(token);
    }

    public  void update(Order o) {

            ordersRef.child(o.id).setValue(o);

    }



    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }

    public void addMenuItems(String name, String description, String category, String price, String availability, String time, String imgUri,  List<String> subItems,String imageName) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, subItems,imageName);
        menuItemsRef.push().setValue(item);
        Uri file = Uri.parse(imgUri);
        StorageReference profileRefStore = storageRef.child("images/menuItems/" + imageName);
        profileRefStore.putFile(file);
        //TODO: add callback may be useful
    }


    public  List<Order> getPendingOrders(FirebaseCallback firebaseCallback) {
        List<Order> pendings = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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


    public void setMenuItems(String id, String name, String category, String description, String price, String availability, String time, String imgUri, List<String> subItems,String imageName) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri,  subItems, imageName);
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
    void putRestaurantProfile(Restaurant user){

        Uri file = Uri.parse(user.imageUri);
        storageRef.child("images/profile/");

        StorageReference profileRefStore = storageRef.child("images/profile/" + user.imageName);
        profileRefStore.putFile(file);
        profileRef.setValue("profile");
        profileRef.child("profile").setValue(user);




    }
    public void getImage(String imageName,String path,Callback UriImg) {

        if (!imageName.equals("")) {
            Log.d("name", imageName);
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
    public void getRestaurantProfile(FirebaseCallbackUser firebaseCallbackUser) {
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.hasChild("profile")) {
                   dataSnapshot = dataSnapshot.child("profile");
                   Restaurant item = dataSnapshot.getValue(Restaurant.class);
                   if (item != null) {

                       firebaseCallbackUser.onCallbak(item);
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


}

interface OnDataFetched<T, E> {
    void onDataFetched(T data);

    void onError(E error);
}

 interface FirebaseCallback{
    void  onCallbak(List<Order> list);

}
interface FirebaseCallbackUser {
    void onCallbak(Restaurant user);
}
interface Callback{
    void onCallback(Uri item);
}