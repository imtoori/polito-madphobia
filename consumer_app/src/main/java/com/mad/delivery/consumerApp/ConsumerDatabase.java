package com.mad.delivery.consumerApp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.RestaurantCategory;

import java.util.HashSet;
import java.util.Set;

public class ConsumerDatabase {
    public static ConsumerDatabase instance = new ConsumerDatabase();
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private ConsumerDatabase() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
    }

    public static ConsumerDatabase getInstance() {
        return instance;
    }

    public interface onCategoriesReceived{
        void  onCallback(Set<String> list);
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

    public Set<String> getRestaurantsIds(final Set<String> chosen, final onCategoriesReceived firebaseCallback) {
        final Set<String> restaurantIds = new HashSet<>();
        // if chosen.size = 0, search for all restaurants
        myRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        RestaurantCategory restCategory = issue.getValue(RestaurantCategory.class);
                        if(restCategory != null) {
                            if (chosen.contains(restCategory.name) || chosen.size() == 0)
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

    public void getRestaurants(Set<String> chosen, String address, final onPreviewRestaurantsReceived firebaseCallback) {
        getRestaurantsIds(chosen, list -> {
            if(list.isEmpty()) {
                // show empty icon
            } else {
                // ask for restaurants
                Log.d("MADAPP", "Restaurants IDS returned!");
                for(String restName : list) {
                    myRef.child("users").child("restaurants").child(restName).child("previewInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PreviewInfo restaurantPreview = dataSnapshot.getValue(PreviewInfo.class);
                            if (restaurantPreview != null) {
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

        myRef.child("users").child("restaurants").child(previewInfo.id).addListenerForSingleValueEvent(new ValueEventListener() {
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


}
