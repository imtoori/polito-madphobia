package com.mad.delivery.restaurant_app;

import java.math.BigDecimal;

public class MenuItemRest {
    final String name, description;
    final Double price;
    final Integer ttl;
    final String imgUrl;

    public MenuItemRest(String name, String description, Double price, Integer ttl, String imgUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ttl = ttl;
        this.imgUrl = imgUrl;
    }
}
