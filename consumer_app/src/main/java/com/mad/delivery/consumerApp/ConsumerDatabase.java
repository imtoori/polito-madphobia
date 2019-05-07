package com.mad.delivery.consumerApp;

import android.net.Uri;
import android.util.Log;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ConsumerDatabase {
    public static ConsumerDatabase instance = new ConsumerDatabase();
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private HashMap <MenuItemRest,Integer> itemSelected;
    private String resturantId;
    FirebaseAuth mAuth;
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
        if(!resturantId.equals(item.id))
            itemSelected.clear();
        if(itemSelected.get(item)==null)
            itemSelected.put(item,value);
        else
            itemSelected.replace(item,value);
    }

    public HashMap<MenuItemRest, Integer> getItemSelected(){
        return itemSelected;
    }

    public void putOrder(Order o){
        Random rand = new Random();
        o.clientId =mAuth.getUid();
        o.id = resturantId;

        o.status =OrderStatus.pending;
        ConsumerDatabase.getInstance().getBikerId(new firebaseCallback<List<String>>() {
            @Override
            public void onCallBack(List<String> item) {
                if(!item.isEmpty()) {
                    int n = rand.nextInt(item.size());
                    Log.d("MADDAPP", "item: " + item.get(n) + " n: " + n);
                    o.bikerId = item.get(n);
                    myRef.child("orders").push().setValue(o);
                }
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
                    firebaseCallback.onCallBack(rest);
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
                Log.d("MADAPP", "Restaurants IDS returned!");
                for(String restName : list) {
                    myRef.child("users").child("restaurants").child(restName).child("profile").child("previewInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PreviewInfo restaurantPreview = dataSnapshot.getValue(PreviewInfo.class);
                            Log.d("MADAPP", restaurantPreview.toString());
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

    public void getRestaurantCategories(final onRestaurantCategoryReceived cb) {
        myRef.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestaurantCategory category = dataSnapshot.getValue(RestaurantCategory.class);
                if(category != null) cb.childAdded(category);
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
                firebaseCallback.onCallBack(bikerIds);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");

            }
        });

    }

    public void getImage(String imageName,String path,firebaseCallback <Uri> firebaseCallback) {

        if (!imageName.equals("")) {
            Log.d("name", imageName);
            // Uri tmp = Uri.parse(imageName);
            StorageReference profileRefStore = storageRef.child(path + imageName);
            profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("DOWNLOAD",uri.toString());

                    firebaseCallback.onCallBack(uri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    firebaseCallback.onCallBack(Uri.EMPTY);
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
                firebaseCallback.onCallBack(completed);
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
                            firebaseCallback.onCallBack(o);

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

    public void updateCreditCustomer(Integer i){
        myRef.child("users").child("restaurant").child(mAuth.getUid()).child("profile").child("credit").setValue(i);
    }


    public interface firebaseCallback<T>{
        void onCallBack(T item);
    }


}
