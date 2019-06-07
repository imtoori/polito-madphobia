package com.mad.delivery.resources;
import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

public class RestaurantCategory {
    public String name;
    public String imageURL;
    public int count;
    @Exclude
    public Drawable imageDrawable;
    @Exclude
    public boolean selected = false;
    public Map<String, Boolean> restaurants;

    public  RestaurantCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public Map<String, Boolean> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Map<String, Boolean> restaurants) {
        this.restaurants = restaurants;
    }
}
