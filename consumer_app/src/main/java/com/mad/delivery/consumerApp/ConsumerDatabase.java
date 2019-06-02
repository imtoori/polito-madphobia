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
import com.mad.delivery.consumerApp.wallet.OrdersAdapter;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.OnImageUploaded;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;
import com.mad.delivery.resources.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsumerDatabase {
    public static ConsumerDatabase instance = new ConsumerDatabase();
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private HashMap<MenuItemRest, Integer> itemSelected;
    private String resturantId;
    public Restaurant restaurant;
    FirebaseAuth mAuth;
    Boolean flag = true;


    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }

    public String getResturantId() {
        return resturantId;
    }

    private ConsumerDatabase() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        itemSelected = new HashMap<MenuItemRest, Integer>();
        resturantId = "";
    }

    public static ConsumerDatabase getInstance() {
        return instance;
    }

    public void checkLogin(String id, OnLogin<User> cb) {
        if (id == null || id.equals("")) {
            Log.d("MADAPP", "checkLogin: id null or empty");
            cb.onFailure();
            return;
        }
        myRef.child("users").child("customers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User rest = dataSnapshot.getValue(User.class);
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


    public void reset() {
        instance = null;
    }

    public interface onRestaurantsIdReceived {
        void onCallback(Set<String> list);
    }

    public interface onAllCategoriesReceived {
        void onReceived(Set<String> categories);
    }

    public interface onPreviewRestaurantsReceived {
        void onCallback(PreviewInfo preview);
    }

    public interface onRestaurantCategoryReceived {
        void childAdded(RestaurantCategory rc);

        void childChanged(RestaurantCategory rc);

        void childMoved(RestaurantCategory rc);

        void childDeleted(RestaurantCategory rc);
    }

    public interface onRestaurantInfoReceived {
        void onReceived(Restaurant rest);
    }

    public void setItemSelected(MenuItemRest item, Integer value) {
        Log.d("MADDAPP", "Il nome del piatto è" + item.name);
        if (!resturantId.equals(item.restaurantId))
            itemSelected.clear();
        if (itemSelected.get(item) == null)
            itemSelected.put(item, value);
        else
            itemSelected.replace(item, value);
    }

    public HashMap<MenuItemRest, Integer> getItemSelected() {
        return itemSelected;
    }

    public void putOrder(Order o, Context context, firebaseCallback<Boolean> firebaseCallback2) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(o.delivery, 1);
        if (addresses.size() > 0) {
            Double latitude = addresses.get(0).getLatitude();
            Double longitude = addresses.get(0).getLongitude();
            Log.d("Address2: ", latitude.toString() + " " + longitude.toString());

            o.latitude = latitude;
            o.longitude = longitude;
        }
        flag = true;

        myRef.child("users").child("restaurants").child(o.restaurantId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    Log.d("TRANS", "mutable null");
                } else {
                    Log.d("TRANS", "mutable not null");
                    Log.e("CREDIT: ", "method " + o.paymentMethod);

                    if (o.paymentMethod.matches("credit")) {
                        Log.e("CREDIT: ", "credit " + o.totalPrice.toString());
                        myRef.child("users").child("customers").child(mAuth.getUid()).child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Double d = dataSnapshot.getValue(Double.class);
                                    Log.d("TRANS", "credito " + d.toString() + " tot: " + o.totalPrice);

                                    if (d > o.totalPrice) {
                                        getMenuItems(o, new firebaseCallback<List<MenuItemRest>>() {
                                            @Override
                                            public void onCallBack(List<MenuItemRest> item) throws IOException {
                                                item.forEach(i -> {
                                                    o.products.forEach(p -> {
                                                        Log.d("TRANS", "id menuItems " + i.id + " id prodotto " + p.idItem + " quantità prodotto: " + p.quantity + " quantità items " + i.availability);
                                                        if (i.id.equals(p.idItem)) {
                                                            if (p.quantity > i.availability)
                                                                flag = false;
                                                            else
                                                                i.availability -= p.quantity;
                                                        }
                                                    });

                                                });
                                                if (flag) {
                                                    item.forEach(i -> {
                                                        myRef.child("users").child("restaurants").child(o.restaurantId).child("menu").child(i.id).setValue(i);
                                                    });
                                                    myRef.child("orders").push().setValue(o);
                                                    myRef.child("users").child("customers").child(mAuth.getUid()).child("credit").setValue(d - o.totalPrice);
                                                }

                                                firebaseCallback2.onCallBack(flag);
                                            }
                                        });

                                    } else {
                                        try {
                                            firebaseCallback2.onCallBack(false);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (o.paymentMethod.matches("cash")) {
                        getMenuItems(o, new firebaseCallback<List<MenuItemRest>>() {
                            @Override
                            public void onCallBack(List<MenuItemRest> item) throws IOException {
                                item.forEach(i -> {
                                    o.products.forEach(p -> {
                                        Log.d("TRANS", "id menuItems " + i.id + " id prodotto " + p.idItem + " quantità prodotto: " + p.quantity + " quantità items " + i.availability);
                                        if (i.id.equals(p.idItem)) {
                                            if (p.quantity > i.availability)
                                                flag = false;
                                            else
                                                i.availability -= p.quantity;
                                        }
                                    });

                                });
                                if (flag) {
                                    item.forEach(i -> {
                                        myRef.child("users").child("restaurants").child(o.restaurantId).child("menu").child(i.id).setValue(i);
                                    });
                                    myRef.child("orders").push().setValue(o);
                                }

                                firebaseCallback2.onCallBack(flag);
                            }
                        });
                    }

                }

                if (flag)
                    return Transaction.success(mutableData);
                else
                    return Transaction.abort();
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });


    }

    public void getRestourant(String resturantId, firebaseCallback<Restaurant> firebaseCallback) {
        myRef.child("users").child("restaurants").child(resturantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant rest = dataSnapshot.getValue(Restaurant.class);
                Log.d("MADAPP", "inside db: " + rest.toString());
                if (rest != null) {
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
                Log.d("MADAPP", "onCanceled: " + this.toString());
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
                        if (restCategory != null) {
                            if (chosen.contains(restCategory.name.toLowerCase()) || chosen.size() == 0)
                                if (restCategory.restaurants != null && restCategory.restaurants.size() != 0)
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

    public void getRestaurants(Set<String> chosen, String address, boolean m, boolean d, final onPreviewRestaurantsReceived firebaseCallback) {
        getRestaurantsIds(chosen, list -> {
            if (list.isEmpty()) {
                // show empty icon
                firebaseCallback.onCallback(null);
            } else {
                // ask for restaurants
                for (String restName : list) {
                    myRef.child("users").child("restaurants").child(restName).child("previewInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PreviewInfo restaurantPreview = dataSnapshot.getValue(PreviewInfo.class);
                            if (restaurantPreview != null) {
                                if (m && restaurantPreview.minOrderCost != 0) {
                                    firebaseCallback.onCallback(null);
                                    return;
                                }
                                if (d && restaurantPreview.deliveryCost != 0) {
                                    firebaseCallback.onCallback(null);
                                    return;
                                }
                                firebaseCallback.onCallback(restaurantPreview);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            firebaseCallback.onCallback(null);
                        }
                    });
                }
            }
        });
    }

    public void getRestaurantCategories(List<RestaurantCategory> categories, final OnFirebaseData<RestaurantCategory> cb) {
        myRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        RestaurantCategory restCategory = issue.getValue(RestaurantCategory.class);
                        if(restCategory != null) cb.onReceived(restCategory);
                    }
                } else {
                    cb.onReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(null);
            }
        });
    }

    public void getRestaurantInfo(PreviewInfo previewInfo, onRestaurantInfoReceived cb) {

        myRef.child("users").child("restaurants").child(previewInfo.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant rest = dataSnapshot.getValue(Restaurant.class);
                Log.d("MADAPP", "inside db: " + rest.toString());
                if (rest != null) {
                    cb.onReceived(rest);
                    restaurant = rest;
                } else {
                    Log.d("MADAPP", "restaurant is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MADAPP", "onCanceled: " + this.toString());
            }
        });
    }

    public Restaurant getRestaurantInLocal() {
        return this.restaurant;
    }

    public void getCategories(onAllCategoriesReceived cb) {
        Log.d("MADAPP", "getCategories is called..");
        Set<String> categories = new HashSet<>();
        myRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        RestaurantCategory restCategory = issue.getValue(RestaurantCategory.class);
                        if (restCategory != null) {
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

    public void getMenuItems(Order o, firebaseCallback<List<MenuItemRest>> firebaseCallback) {
        myRef.child("users").child("restaurants").child(o.restaurantId).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItemRest> MenuItems = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        MenuItemRest m = snapshot.getValue(MenuItemRest.class);
                        o.products.forEach(p -> {
                            Log.d("TRANS", "id prodotto: " + p.idItem + " id item " + m.id);
                            if (p.idItem.equals(m.id))
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

    public void getBikerId(firebaseCallback<List<String>> firebaseCallback) {
        myRef.child("users").child("biker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bikerIds = new ArrayList<>();
                if (dataSnapshot.exists()) {

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

    public void getImage(String imageName, String path, firebaseCallback<Uri> firebaseCallback) {
        if (imageName == null || !imageName.equals("")) {
            // Uri tmp = Uri.parse(imageName);
            StorageReference profileRefStore = storageRef.child(path + imageName);
            profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("DOWNLOAD", uri.toString());

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

    public List<Order> getCompletedOrders(firebaseCallback<List<Order>> firebaseCallback) {

        List<Order> completed = new ArrayList<>();
        myRef.child("order").orderByChild("clientId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);

                        if (o.status.toString().equals("completed")) {
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

    public void getAllCostumerOrders(String clientID, OnFirebaseData<Order> cb) {
        myRef.child("orders").orderByChild("clientId").equalTo(clientID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        o.id = issue.getKey();
                        cb.onReceived(o);
                    }
                } else {
                    cb.onReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(null);
            }
        });

    }

/*    public void getAllCostumerOrders(String clientID, OnFirebaseData<Order> cb) {
        myRef.child("orders").orderByChild("clientId").equalTo(clientID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        o.id = issue.getKey();
                        cb.onReceived(o);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

    public void checkCoupon(String code, OnFirebaseData<CreditCode> cb) {
        myRef.child("creditsCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CreditCode cc = null;
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        CreditCode o = issue.getValue(CreditCode.class);
                        if (o.code.equals(code)) {
                            cc = o;
                            break;
                        }
                    }
                    cb.onReceived(cc);
                } else {
                    cb.onReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(null);
            }
        });
    }

    public void putUserProfile(User user) {
        user.id = mAuth.getUid();
        Uri file = Uri.parse(user.imageUri);

        StorageReference profileRefStore = storageRef.child("images/profile/" + user.imageName);
        profileRefStore.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
        Log.d("TAG:", "mauth:  " + mAuth.getUid());
        myRef.child("users").child("customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "SONO ENTRATO");

            }
        });

    }


    public void updateCreditCustomer(Double i, firebaseCallback<Boolean> firebaseCallback) {
        myRef.child("users").child("customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("credit")) {
                    Double credit = dataSnapshot.child("credit").getValue(Double.class);
                    setValueCredit(credit + i);
                    try {
                        firebaseCallback.onCallBack(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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

    public void setValueCredit(Double i) {
        myRef.child("users").child("customers").child(mAuth.getUid()).child("credit").setValue(i);
    }

    public void saveFeedback(Order o, Feedback feedback, OnFirebaseData<Boolean> cb) {
        myRef.child("orders").child(o.id).child("feedbackIsPossible").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    cb.onReceived(false);
                    return;
                }
                Boolean value = dataSnapshot.getValue(Boolean.class);
                if (!value) {
                    cb.onReceived(false);
                    return;
                }
                getRestourant(o.restaurantId, r -> {
                    if (r != null) {
                        if (r.previewInfo.scoreValue == null) {
                            r.previewInfo.scoreValue = (feedback.serviceRestaurantVote + feedback.foodVote) / 2;
                            r.previewInfo.scoreCount = 1;
                        } else {
                            if (r.previewInfo.scoreCount == null) r.previewInfo.scoreCount = 0;
                            r.previewInfo.scoreValue = (r.previewInfo.scoreValue * r.previewInfo.scoreCount) + ((feedback.serviceRestaurantVote + feedback.foodVote) / 2);
                            r.previewInfo.scoreCount++;
                            r.previewInfo.scoreValue = r.previewInfo.scoreValue / r.previewInfo.scoreCount;
                        }
                        myRef.child("users").child("restaurants").child(r.previewInfo.id).child("previewInfo").child("scoreValue").setValue(r.previewInfo.scoreValue);
                        myRef.child("users").child("restaurants").child(r.previewInfo.id).child("previewInfo").child("scoreCount").setValue(r.previewInfo.scoreCount);
                    }
                    myRef.child("orders").child(o.id).child("feedbackIsPossible").setValue(false, (dErr, db) -> {
                        String id = myRef.child("feedbacks").push().getKey();
                        feedback.id = id;
                        myRef.child("feedbacks").child(feedback.id).setValue(feedback, (databaseError, databaseReference) -> {
                            cb.onReceived(true);
                        });
                    });
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(false);
            }
        });


    }


    public void getUserProfile(String id, OnFirebaseData<User> cb) {
        myRef.child("users").child("customers").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User item = dataSnapshot.getValue(User.class);
                    if (item != null) {
                        Log.d("MADAPP", "getUserProfile returned: " + item.toString());
                        cb.onReceived(item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void downloadImage(String id, String path, String imageName, OnImageDownloaded uriImage) {
        if (imageName == null || imageName.equals("")) {
            uriImage.onReceived(Uri.EMPTY);
            return;
        }
        StorageReference profileRefStore = storageRef.child(id).child(path).child(imageName);
        profileRefStore.getDownloadUrl().addOnSuccessListener(uri -> {
            uriImage.onReceived(uri);
        }).addOnFailureListener(exception -> {
            exception.printStackTrace();
            uriImage.onReceived(Uri.EMPTY);
        });
    }

    public void uploadImage(String id, Uri uri, String path, String imageName, OnImageUploaded cb) {
        StorageReference profileRefStore = storageRef.child(id).child(path).child(imageName);
        profileRefStore.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Log.d("MADAPP", "The image profile has been uploaded.");
            cb.onFinished();
        });
    }

    public void updateUserProfile(User r, Uri image) {
        Log.d("MADAPP", "arg=" + r.toString());
        if (image == null || image.toString().equals("") || image.toString().contains("https:")) {
            myRef.child("users").child("customers").child(r.id).setValue(r);
            return;
        }

        uploadImage(r.id, image, "profile", image.getLastPathSegment(), () -> {
            r.imageName = image.getLastPathSegment();
            myRef.child("users").child("customers").child(r.id).setValue(r);
        });
    }

    public void getReviews(String restaurantID, OnFirebaseData<Feedback> cb) {
        myRef.child("feedbacks").orderByChild("restaurantID").equalTo(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Feedback o = issue.getValue(Feedback.class);
                        if (o != null) {
                            cb.onReceived(o);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}

