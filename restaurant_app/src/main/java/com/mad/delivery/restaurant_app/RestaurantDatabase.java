package com.mad.delivery.restaurant_app;

import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MenuOffer;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;
import com.mad.delivery.restaurant_app.auth.OnLogin;
import com.mad.delivery.restaurant_app.menu.OnMenuChanged;
import com.mad.delivery.restaurant_app.menu.OnMenuReceived;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

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
    private static Restaurant restaurant;

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

        getRestaurantProfile(new FireBaseCallBack<Restaurant>() {
            @Override
            public void onCallback(Restaurant user) {
                restaurant=user;
            }

            @Override
            public void onCallbackList(List<Restaurant> list) {

            }
        });
    }

    private void getRestaurantProfile(FireBaseCallBack<Restaurant> restaurantFireBaseCallBack) {
        if(mAuth.getUid()!=null) {
            restaurantRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Restaurant item = dataSnapshot.getValue(Restaurant.class);
                    if (item != null) {
                        restaurantFireBaseCallBack.onCallback(item);
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
    }

    public void reset() {
        instance = null;
    }

    void updateToken(String id, String token) {
        Log.d("MADAPP", "id=" + id  +" , token=" + token);
        restaurantRef.child(id).child("token").setValue(token);
    }

    public void update(Order o) {
        ordersRef.child(o.id).setValue(o);
    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            //return DateTimeComparator.getInstance().compare(new DateTime(second.orderFor), new DateTime(first.orderFor));
            return second.orderFor.compareTo(first.orderFor);
        }
    }

    public void updateOffer(String restaurantID, MenuOffer menuOffer, OnFirebaseData<MenuOffer> cb) {
        if(restaurantID == null) {
            return;
        }
        menuOffer.id = restaurantRef.child(restaurantID).child("offers").push().getKey();
        restaurantRef.child(restaurantID).child("offers").child(menuOffer.id).setValue(menuOffer);
        cb.onReceived(menuOffer);
    }

    public void removeMenuOffer(String restaurantID, MenuOffer item, OnFirebaseData<MenuOffer> cb) {
        if(restaurantID == null) {
            return;
        }
        restaurantRef.child(restaurantID).child("offers").child(item.id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cb.onReceived(item);
            }
        });
    }

    public void getOffers(String restaurantID, OnFirebaseData<List<MenuOffer>> cb) {
        if(restaurantID == null) {
            return;
        }
        restaurantRef.child(restaurantID).child("offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuOffer> offers = new ArrayList<>();
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    MenuOffer item = snapshot.getValue(MenuOffer.class);
                    if(item != null) {
                        offers.add(item);
                    }
                }
                cb.onReceived(offers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(new ArrayList<>());
            }
        });
    }

    public void getMenu(String restaurantID, OnMenuReceived cb) {
        if(restaurantID == null) {
            return;
        }
        restaurantRef.child(restaurantID).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories;
                Map<String, List<MenuItemRest>> menu = new HashMap<>();
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterator) {
                    MenuItemRest item = snapshot.getValue(MenuItemRest.class);
                    if (item != null) {
                        menu.putIfAbsent(item.category, new ArrayList<>());
                        menu.get(item.category).add(item);
                    }
                }
                categories = new ArrayList<>(menu.keySet());
                cb.menuReceived(menu, categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.menuReceived(new HashMap<>(), new ArrayList<>());
            }
        });
    }



    public void removeMenuItem(String restaurantID, MenuItemRest item, OnMenuReceived cb) {
        if(restaurantID == null) {
            return;
        }
        if(item.id == null) {
            return;
        }
        restaurantRef.child(restaurantID).child("menu").child(item.id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cb.itemRemoved(item);
            }
        });
    }

    public void updateMenuItem(String restaurantID, MenuItemRest menuItem, Uri imageMenuItem) {
        if(restaurantID == null) {
            return;
        }
        if(menuItem.id == null || menuItem.id.equals(""))
            menuItem.id = restaurantRef.child(restaurantID).child("menu").push().getKey();

        if (imageMenuItem == null || imageMenuItem.toString().equals("") || imageMenuItem.toString().contains("https:")) {
            restaurantRef.child(restaurantID).child("menu").child(menuItem.id).setValue(menuItem);
            return;
        }
        uploadImage(restaurantID, imageMenuItem, "menu", imageMenuItem.getLastPathSegment(), () -> {
            menuItem.imageName = imageMenuItem.getLastPathSegment();
            restaurantRef.child(restaurantID).child("menu").child(menuItem.id).setValue(menuItem);
        });
    }

    public void getPendingOrders(String restaurantID, OnFirebaseData<List<Order>> cb) {
        List<Order> pendings = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                Collections.sort(pendings, myDateComparator);
                cb.onReceived(pendings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(pendings);
            }
        });
    }

    public void getPreparingAndReadyOrders(String restaurantID, OnFirebaseData<List<Order>> cb) {
        List<Order> preparing = new ArrayList<>();

        ordersRef.orderByChild("restaurantId").equalTo(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                        if (o.status.toString().equals("preparing") || o.status.toString().equals("ready")) {
                            o.id = issue.getKey();
                            preparing.add(o);
                        }
                    }
                }
                Collections.sort(preparing, myDateComparator);
                cb.onReceived(preparing);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(preparing);
            }
        });
    }

    public void getCompletedOrders(String restaurantID, OnFirebaseData<List<Order>> cb) {
        List<Order> completed = new ArrayList<>();
        ordersRef.orderByChild("restaurantId").equalTo(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);

                        if (o.status.toString().equals("completed") || o.status.toString().equals("canceled") || o.status.toString().equals("delivered")) {
                            o.id = issue.getKey();
                            completed.add(o);
                        }
                    }
                }
                Collections.sort(completed, myDateComparator);
                cb.onReceived(completed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(completed);
            }
        });
    }


    public void updateRestaurantProfile(Restaurant r, Uri image) {
        Log.d("MADAPP", "## UpdateRestaurantProfile: " + r.toString());
        getMenu(r.previewInfo.id, new OnMenuReceived() {
            @Override
            public void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories) {
                if(r.menu == null) r.menu = new HashMap<>();
                menu.entrySet().stream().forEach((entry) -> {
                    entry.getValue().stream().forEach(m -> {
                        r.menu.put(m.id, m);
                    });
                });
                if (image == null || image.toString().equals("") || image.toString().contains("https:")) {
                    restaurantRef.child(r.previewInfo.id).setValue(r);
                    return;
                }

                uploadImage(r.previewInfo.id, image, "profile", image.getLastPathSegment(), () -> {
                    r.previewInfo.imageName = image.getLastPathSegment();
                    restaurantRef.child(r.previewInfo.id).setValue(r);
                });
            }

            @Override
            public void itemRemoved(MenuItemRest item) {

            }
        });


    }

    public void updateRestaurantVisibility(String restaurantID, boolean value, OnFirebaseData<Boolean> cb) {
        restaurantRef.child(restaurantID).child("visible").setValue(value, (databaseError, databaseReference) -> {
            cb.onReceived(value);
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
    public void getBikersClosest(FireBaseCallBack<TreeMap<Double,Biker>> firebaseCallback) {
        myRef.child("users").child("biker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<Double,Biker> bikerIdDistance = new TreeMap<>();
                if(dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if(snapshot.getValue(Biker.class).status=true) {
                            Log.d("TAG:", restaurant.toString());
                            bikerIdDistance.put(Haversine.distance(restaurant.latitude, restaurant.longitude, snapshot.getValue(Biker.class).latitude, snapshot.getValue(Biker.class).longitude),snapshot.getValue(Biker.class));
                        }
                    }
                }
                firebaseCallback.onCallback(bikerIdDistance);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");

            }
        });
    }


    public void getBikerId(FireBaseCallBack<String> firebaseCallback) {
        myRef.child("users").child("biker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<Double,String> bikerIdDistance = new TreeMap<>();
                List<String> bikerIds = new ArrayList<>();
                if(dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : iterator) {
                        if(snapshot.getValue(Biker.class).status=true)
                        bikerIds.add(snapshot.getKey());
                        Log.d("TAG:",restaurant.toString());
                        bikerIdDistance.put(Haversine.distance(restaurant.latitude,restaurant.longitude,snapshot.getValue(Biker.class).latitude,snapshot.getValue(Biker.class).longitude),snapshot.getKey());
                    }
                }

                firebaseCallback.onCallback(bikerIdDistance.firstEntry().getValue());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE: ", "Dato cancellato");
            }
        });
    }

    public void getBiker(String id, OnFirebaseData<Biker> cb) {
        if(id == null || id.equals("")) {
            cb.onReceived(null);
            return;
        }
        myRef.child("users").child("biker").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Biker b = dataSnapshot.getValue(Biker.class);
                    cb.onReceived(b);
                } else {
                    Log.d("MADAPP", "checkLogin: dataSnapshop doesn't exists." );
                    cb.onReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cb.onReceived(null);
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
    public void getReviews(String restaurantID, OnFirebaseData< Feedback> cb) {
        myRef.child("feedbacks").orderByChild("restaurantID").equalTo(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Feedback o = issue.getValue(Feedback.class);
                        if(o != null) {
                            Log.d("MADAPP", o.toString());
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

    public void getPopularTiming(OnFirebaseData<List<BarEntry>> cb){
        myRef.child("orders").orderByChild("restaurantId").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer hour [] = new Integer [24];
                Arrays.fill(hour,0);
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order o = issue.getValue(Order.class);
                            hour[DateTime.parse(o.orderFor).getHourOfDay()]++;

                    }
                    List <BarEntry> list = new ArrayList<>();
                    for (Integer i=0;i<hour.length;i++){
                        list.add(new BarEntry((float)i,(float)hour[i]));
                    }
                    cb.onReceived(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}



