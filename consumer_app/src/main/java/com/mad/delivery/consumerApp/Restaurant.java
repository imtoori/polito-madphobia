package com.mad.delivery.consumerApp;

public class Restaurant {
    public String name;
    public String description;
    public String urlImage;

    public Restaurant() {}
    public Restaurant(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public Restaurant(Restaurant other) {
        this.name = other.name;
        this.description = other.description;

    }
}
