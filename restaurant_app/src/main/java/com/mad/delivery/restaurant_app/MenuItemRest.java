package com.mad.delivery.restaurant_app;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRest {
    public String name, description;
    public Double price;
    public Integer ttl;
    public String imgUrl;
    public String category;
    public String id;
    public Integer availability;
   // protected Uri imageUri;
    List<String> subItems = new ArrayList<>();

    public MenuItemRest(String name, String category, String description, Double price, Integer availability, Integer ttl, String imgUrl,  List<String> subItems) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ttl = ttl;
        this.imgUrl = imgUrl;
        this.id = id;
        this.category = category;
        this.availability = availability;
   //     this.imageUri = url;
        this.subItems = subItems;
    }

    public MenuItemRest() {
    }

}