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
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

final public class RestaurantDatabase {
    private static RestaurantDatabase instance;
    private static MyDateComparator myDateComparator;
    private DatabaseReference restaurantRef;
    private DatabaseReference menuItemsRef;
    private DatabaseReference ordersRef;
    private DatabaseReference categoriesRef;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;

    public static RestaurantDatabase getInstance() {
        if (instance == null) {
            instance = new RestaurantDatabase();
        }
        return instance;
    }
    private RestaurantDatabase() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        restaurantRef = database.getReference("users/restaurants/");
        menuItemsRef = database.getReference("users/restaurants/");
        ordersRef = database.getReference("orders");
        storageRef = FirebaseStorage.getInstance().getReference();
        categoriesRef = database.getReference().child("categories");
    }

    public void reset() {
        instance = null;
    }


    void updateToken(String id, String token) {
        restaurantRef.child(id).child("token").setValue(token);
    }

    public void update(Order o) {
        ordersRef.child(o.id).setValue(o);
    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }

    public void addMenuItems(String name, String description, String category, String price, String availability, String time, String imgUri, List<String> subItems, String imageName) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, "", Uri.parse(imgUri), subItems, imageName);
        item.restaurantId = mAuth.getUid();
        item.id = menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").push().getKey();
        menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").child(item.id).setValue(item);

        Log.d("MADDAP", item.name + " " + item.category);
        // menuItemsRef.child(item.id).child("id").setValue(item.id);
        Uri file = Uri.parse(imgUri);
        StorageReference profileRefStore = storageRef.child(mAuth.getUid()).child("images/menuItems/" + imageName);
        profileRefStore.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileRefStore.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        //Do what you want with the url
                        item.imageDownload = downloadUrl.toString();
                        // menuItemsRef.push().setValue(item);

                    }
                });
            }
        });
    }


    public List<Order> getPendingOrders(FireBaseCallBack<Order> firebaseCallback) {
        List<Order> pendings = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if (o.status.toString().equals("pending")) {
                            o.id = issue.getKey();
                            pendings.add(o);
                        }
                    }
                }
                firebaseCallback.onCallbackList(pendings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Collections.sort(pendings, myDateComparator);
        return pendings;
    }

    public List<Order> getPreparingOrders(FireBaseCallBack<Order> firebaseCallback) {
        List<Order> preparing = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if (o.status.toString().equals("preparing")) {
                            o.id = issue.getKey();
                            preparing.add(o);
                        }
                    }
                }
                firebaseCallback.onCallbackList(preparing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return preparing;

    }

    public List<Order> getCompletedOrders(FireBaseCallBack<Order> firebaseCallback) {
        if (instance == null) {
            instance = new RestaurantDatabase();
        }
        List<Order> completed = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                firebaseCallback.onCallbackList(completed);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return completed;
    }


    public void setMenuItems(String id, String name, String category, String description, String price, String availability, String time, String imgUri, List<String> subItems, String imageName) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, "", Uri.parse(imgUri), subItems, imageName);
        menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").child(id).setValue(item);
        //TODO add callback
    }


    public void getMenuItems(OnDataFetched<List<MenuItemRest>, String> onDataFetched) {
        menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").addValueEventListener(new ValueEventListener() {
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

    public void removeMenuItem(MenuItemRest menuItemRest) {
        menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").child(menuItemRest.id).removeValue();
        //TODO add callback
    }

    public MenuItemRest getMenuItem(String id, OnDataFetched<MenuItemRest, String> onDataFetched) {
        menuItemsRef.child(mAuth.getUid()).child("profile").child("menuItems").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void updateRestaurantProfile(Restaurant r, Uri image) {
        Log.d("MADAPP", "## UpdateRestaurantProfile: " + r.toString());
        if (image == null || image.toString().equals("") || image.toString().contains("https:")) {
            restaurantRef.child(r.previewInfo.id).setValue(r);
            Log.d("MADAPP", "restaurant ID= " + r.previewInfo.id);
            return;
        }

        uploadImage(r.previewInfo.id, image, "profile", image.getLastPathSegment(), () -> {
            r.previewInfo.imageName = image.getLastPathSegment();
            restaurantRef.child(r.previewInfo.id).setValue(r);
        });

    }

    public void putRestaurantIntoCategory(String idRestaurant, Set<String> categories) {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    RestaurantCategory item = snapshot.getValue(RestaurantCategory.class);
                    if (item != null) {
                        if (item.restaurants != null && item.restaurants.containsKey(idRestaurant) && !categories.contains(item.name.toLowerCase())) {
                            categoriesRef.child(item.name.toLowerCase()).child("restaurants").child(idRestaurant).removeValue();
                        } else if (item.restaurants == null && categories.contains(item.name.toLowerCase())) {
                            categoriesRef.child(item.name.toLowerCase()).child("restaurants").child(idRestaurant).setValue(true);
                        } else if (item.restaurants != null && !item.restaurants.containsKey(idRestaurant) && categories.contains(item.name.toLowerCase())) {
                            categoriesRef.child(item.name.toLowerCase()).child("restaurants").child(idRestaurant).setValue(true);
                        }
                    } else {

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

    public void getRestaurantProfile(String id, OnFirebaseData<Restaurant> cb) {
        restaurantRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Restaurant item = dataSnapshot.getValue(Restaurant.class);
                    if (item != null) {
                        Log.d("MADAPP", "getRestaurantProfile returned: " + item.toString());
                        cb.onReceived(item);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void getCategories(onAllCategoriesReceived cb) {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    RestaurantCategory item = snapshot.getValue(RestaurantCategory.class);
                    if (item != null) {
                        categories.add(item.name);
                    }
                }
                cb.onCompleted(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onCompleted(new ArrayList<>());
            }
        });
    }

    public void getCategories(String restaurantID, onAllCategoriesReceived cb) {
        restaurantRef.child(restaurantID).child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    String item = snapshot.getKey();
                    if (item != null) {
                        categories.add(item);
                    }
                }
                Log.d("MADAPP", "Categories: ->" + categories.toString());
                cb.onCompleted(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onCompleted(new ArrayList<>());
            }
        });
    }

    public interface onAllCategoriesReceived {
        void onCompleted(List<String> categories);

    }


    public void getBikerId(FireBaseCallBack<String> firebaseCallback) {
        myRef.child("users").child("biker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bikerIds = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if (snapshot.getValue(Biker.class).status = true)
                            bikerIds.add(snapshot.getKey());


                    }
                }
                firebaseCallback.onCallbackList(bikerIds);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");

            }
        });
    }

    public void checkLogin(String id, OnLogin<Restaurant> cb) {
        if(id == null || id.equals("")) {
            Log.d("MADAPP", "checkLogin: id null or empty" );
            cb.onFailure();
            return;
        }
        myRef.child("users").child("restaurants").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Restaurant rest = dataSnapshot.getValue(Restaurant.class);
                    Log.d("MADAPP", "checkLogin: OK" );
                    cb.onSuccess(rest);
                } else {
                    Log.d("MADAPP", "checkLogin: dataSnapshop doesn't exists." );

                    cb.onFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onFailure();
            }
        });
    }


}



