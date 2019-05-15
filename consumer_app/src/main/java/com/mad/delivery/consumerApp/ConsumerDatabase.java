package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConsumerDatabase {
    public static ConsumerDatabase instance = new ConsumerDatabase();
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private HashMap <MenuItemRest,Integer> itemSelected;
    private String resturantId;
    private Order order;
    public Restaurant restaurant;
    FirebaseAuth mAuth;
     Boolean flag=true;


    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }
    public String getResturantId(){
        return resturantId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public Order getOrder(){
        return order;
    }

    private ConsumerDatabase() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        itemSelected = new HashMap<MenuItemRest,Integer>();
        resturantId ="";

    }

    public static ConsumerDatabase getInstance() {
        return instance;
    }

    public void reset() {
        instance = null;
    }

    public interface onRestaurantsIdReceived{
        void  onCallback(Set<String> list);
    }
    public interface onAllCategoriesReceived {
        void onReceived(Set<String> categories);
    }
    public interface onPreviewRestaurantsReceived{
        void  onCallback(PreviewInfo preview);
    }
    public interface onRestaurantCategoryReceived {
        void  childAdded(RestaurantCategory rc);
        void  childChanged(RestaurantCategory rc);
        void  childMoved(RestaurantCategory rc);
        void  childDeleted(RestaurantCategory rc);
    }

    public interface onRestaurantInfoReceived {
        void onReceived(Restaurant rest);
    }

    public void setItemSelected(MenuItemRest item, Integer value){
        Log.d("MADDAPP","Il nome del piatto è"+item.name);
        if(!resturantId.equals(item.restaurantId))
            itemSelected.clear();
        if(itemSelected.get(item)==null)
            itemSelected.put(item,value);
        else
            itemSelected.replace(item,value);
    }

    public HashMap<MenuItemRest, Integer> getItemSelected(){
        return itemSelected;
    }

    public void putOrder(Order o,Context context,firebaseCallback<Boolean> firebaseCallback) throws IOException {
        o.clientId =mAuth.getUid();
        o.restaurantId = resturantId;
        o.status =OrderStatus.pending;

        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(o.delivery.toString(), 1);
        if(addresses.size() > 0) {
            Double latitude= addresses.get(0).getLatitude();
            Double longitude= addresses.get(0).getLongitude();
            Log.d("Address2: ",latitude.toString()+" "+longitude.toString());

            o.latitude=latitude;
            o.longitude=longitude;
        }




       flag=true;
        myRef.child("users").child("restaurants").child(o.restaurantId).child("profile").runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if(mutableData.getValue()==null) {
                    Log.d("TRANS", "mutable null");
                }
                else {
                    Log.d("TRANS", "mutable not null");

                    getMenuItems(o, new firebaseCallback<List<MenuItemRest>>() {
                        @Override
                        public void onCallBack(List<MenuItemRest> item) throws IOException {
                            item.forEach(i->{
                                o.products.forEach(p->{
                                    Log.d("TRANS", "id menuItems "+i.id +" id prodotto "+p.idMenuItems +" quantità prodotto: "+p.quantity +" quantità items "+ i.availability );
                                    if(i.id.equals(p.idMenuItems)) {
                                        if (p.quantity > i.availability)
                                            flag = false;
                                        else
                                            i.availability -= p.quantity;
                                    }
                                });

                            });
                            if(flag){
                                item.forEach(i->{
                                    myRef.child("users").child("restaurants").child(o.restaurantId).child("profile").child("menuItems").child(i.id).setValue(i);
                                });
                                myRef.child("orders").push().setValue(o);
                            }
                            firebaseCallback.onCallBack(flag);
                        }
                    });
                }





                if(flag)
                return Transaction.success(mutableData);
                else
                    return Transaction.abort();

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });


    }

    public void getRestourant(String resturantId, firebaseCallback<Restaurant> firebaseCallback){
        myRef.child("users").child("restaurants").child(resturantId).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant rest = dataSnapshot.getValue(Restaurant.class);
                Log.d("MADAPP", "inside db: " + rest.toString());
                if(rest != null) {
                    try {
                        firebaseCallback.onCallBack(rest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("MADAPP", "restaurant is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MADAPP", "onCanceled: "+ this.toString());
            }
        });
    }

    public Set<String> getRestaurantsIds(final Set<String> chosen, final onRestaurantsIdReceived firebaseCallback) {
        final Set<String> restaurantIds = new HashSet<>();
        // if chosen.size = 0, search for all restaurants
        Log.d("MADAPP", "in database --> " + chosen.toString());
        myRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        RestaurantCategory restCategory = issue.getValue(RestaurantCategory.class);
                        if(restCategory != null) {
                            if (chosen.contains(restCategory.name.toLowerCase()) || chosen.size() == 0)
                                if(restCategory.restaurants != null && restCategory.restaurants.size() != 0)
                                    restaurantIds.addAll(restCategory.restaurants.keySet());
                        }
                    }
                }
                firebaseCallback.onCallback(restaurantIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseCallback.onCallback(restaurantIds);
            }
        });
        return restaurantIds;
    }

    public void getRestaurants(Set<String> chosen, String address, boolean m, boolean d,  final onPreviewRestaurantsReceived firebaseCallback) {
        getRestaurantsIds(chosen, list -> {
            if(list.isEmpty()) {
                // show empty icon
            } else {
                // ask for restaurants
                for(String restName : list) {
                    myRef.child("users").child("restaurants").child(restName).child("profile").child("previewInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PreviewInfo restaurantPreview = dataSnapshot.getValue(PreviewInfo.class);
                            if (restaurantPreview != null) {
                                if(m && restaurantPreview.minOrderCost != 0) {
                                    return;
                                }
                                if(d && restaurantPreview.deliveryCost != 0) {
                                    return;
                                }
                                firebaseCallback.onCallback(restaurantPreview);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void getRestaurantCategories(List<RestaurantCategory> categories,final onRestaurantCategoryReceived cb) {
        myRef.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestaurantCategory category = dataSnapshot.getValue(RestaurantCategory.class);
                if(category != null) {
                    flag=true;
                    categories.forEach(x -> {
                        if (x.name.equals(category.name)) flag = false;
                    });
                    if (flag) {
                        cb.childAdded(category);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestaurantCategory category = dataSnapshot.getValue(RestaurantCategory.class);
                if(category != null) cb.childChanged(category);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                RestaurantCategory category = dataSnapshot.getValue(RestaurantCategory.class);
                if(category != null) cb.childDeleted(category);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestaurantCategory category = dataSnapshot.getValue(RestaurantCategory.class);
                if(category != null) cb.childMoved(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getRestaurantInfo(PreviewInfo previewInfo, onRestaurantInfoReceived cb) {

        myRef.child("users").child("restaurants").child(previewInfo.id).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant rest = dataSnapshot.getValue(Restaurant.class);
                Log.d("MADAPP", "inside db: " + rest.toString());
                if(rest != null) {
                    cb.onReceived(rest);
                    restaurant = rest;
                } else {
                    Log.d("MADAPP", "restaurant is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MADAPP", "onCanceled: "+ this.toString());
            }
        });
    }

    public Restaurant getRestaurantInLocal(){
        return this.restaurant;
    }

    public void getCategories(onAllCategoriesReceived cb) {
        Set<String> categories = new HashSet<>();
        myRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        RestaurantCategory restCategory = issue.getValue(RestaurantCategory.class);
                        if(restCategory != null) {
                           categories.add(restCategory.name);
                        }
                    }
                }
                cb.onReceived(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cb.onReceived(new HashSet<>());
            }
        });
    }

    public void getMenuItems(Order o,firebaseCallback<List<MenuItemRest>> firebaseCallback){
        myRef.child("users").child("restaurants").child(o.restaurantId).child("profile").child("menuItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItemRest> MenuItems = new ArrayList<>();
                if(dataSnapshot.exists()) {

                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        MenuItemRest m = snapshot.getValue(MenuItemRest.class);
                        o.products.forEach(p->{
                            Log.d("TRANS", "id prodotto: "+p.idMenuItems+" id item "+m.id);
                            if(p.idMenuItems.equals(m.id))
                                MenuItems.add(m);
                        });



                    }
                }
                try {
                    firebaseCallback.onCallBack(MenuItems);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");

            }
        });

    }

    public void getBikerId(firebaseCallback<List<String>> firebaseCallback){
        myRef.child("users").child("biker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bikerIds = new ArrayList<>();
                if(dataSnapshot.exists()) {

                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {

                            // item.id = snapshot.getKey();

                            bikerIds.add(snapshot.getKey());


                    }
                }
                try {
                    firebaseCallback.onCallBack(bikerIds);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");

            }
        });

    }

    public void getImage(String imageName,String path,firebaseCallback <Uri> firebaseCallback) {

        if (imageName==null ||!imageName.equals("")) {
            // Uri tmp = Uri.parse(imageName);
            StorageReference profileRefStore = storageRef.child(path + imageName);
            profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("DOWNLOAD",uri.toString());

                    try {
                        firebaseCallback.onCallBack(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    try {
                        firebaseCallback.onCallBack(Uri.EMPTY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public  List<Order> getCompletedOrders(firebaseCallback<List<Order>> firebaseCallback) {

        List<Order> completed = new ArrayList<>();
        myRef.child("order").orderByChild("clientId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                try {
                    firebaseCallback.onCallBack(completed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return completed;
    }

    public  List<Order> getAllCostumerOrders(firebaseCallback<List<Order>> firebaseCallback) {

        List<Order> completed = new ArrayList<>();
        myRef.child("orders").orderByChild("clientId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                            o.id = issue.getKey();
                            completed.add(o);

                    }
                }
                try {
                    firebaseCallback.onCallBack(completed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return completed;
    }
    public void checkCreditCode(String code,firebaseCallback<CreditCode> firebaseCallback){
        myRef.child("creditsCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        CreditCode o = issue.getValue(CreditCode.class);
                        if(o.code.equals(code)){
                            try {
                                firebaseCallback.onCallBack(o);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("MADDAPP","Funzione credit code fail");
            }
        });
    }

    public void putUserProfile(User user){
        user.id = mAuth.getUid();
        Uri file = Uri.parse(user.imageUri);

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
                        myRef.child("users").child("customers").child(mAuth.getUid()).child("profile").setValue(user);

                    }
                });
            }
        });
      //  myRef.child("users").child("customers").child(mAuth.getUid()).setValue("profile");
        myRef.child("users").child("customers").child(mAuth.getUid()).child("profile").setValue(user);

    }

    public void getUserId(firebaseCallback<User> firebaseCallbackUser) {
        myRef.child("users").child("customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("profile")) {
                    dataSnapshot = dataSnapshot.child("profile");
                    User item = dataSnapshot.getValue(User.class);
                    if (item != null) {

                        try {
                            firebaseCallbackUser.onCallBack(item);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //   userStringOnDataFetched.onError("data not found");
                        Log.d("DATABASE: ", "Elemento nullo");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "SONO ENTRATO");

            }
        });

    }


    public void updateCreditCustomer(Double i,firebaseCallback<Boolean> firebaseCallback){
       myRef.child("users").child("customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){


           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.hasChild("profile")) {
                   Double credit = dataSnapshot.child("profile").child("credit").getValue(Double.class);
                   setValueCredit(credit+i);
                   try {
                       firebaseCallback.onCallBack(true);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               else{
                   try {
                       firebaseCallback.onCallBack(false);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }


       });

    }

    public void setValueCredit(Double i){
        myRef.child("users").child("customers").child(mAuth.getUid()).child("profile").child("credit").setValue(i);
    }




}

