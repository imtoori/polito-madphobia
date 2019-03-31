package com.mad.delivery.consumerApp;

import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String phoneNumber;
    protected String email;
    protected String deliveryAddress;
    protected String description;
    protected String imageUri;

    public User(String name, String phoneNumber, String email, String deliveryAddress, String description, String imageUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.description = description;
        this.imageUri = imageUri;
    }
}
