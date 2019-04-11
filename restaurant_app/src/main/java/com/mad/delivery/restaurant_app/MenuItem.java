package com.mad.delivery.restaurant_app;

import java.math.BigDecimal;

public class MenuItem {
    final String name, description;
    final BigDecimal price;
    final int ttl;
    final String imgUrl;

    public MenuItem(String name, String description, BigDecimal price, int ttl, String imgUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ttl = ttl;
        this.imgUrl = imgUrl;
    }
}
