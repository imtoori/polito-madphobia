package com.mad.delivery.restaurant_app;

import android.net.Uri;

import java.math.BigDecimal;

public class MenuItemRest {
    public String name, description;
    public Double price;
    public Integer ttl;
    public String imgUrl;
    public Integer id;
    protected Uri imageUri;


    public MenuItemRest(String name, String description, Double price, Integer ttl, String imgUrl, Integer id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ttl = ttl;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public MenuItemRest(){}

}
