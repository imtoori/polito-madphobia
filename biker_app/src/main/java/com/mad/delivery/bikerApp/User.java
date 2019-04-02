package com.mad.delivery.bikerApp;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String phoneNumber;
    protected String email;
    protected String deliveryAddress;
    protected String description;
    protected Uri imageUri;

    public User() {}
    public User(String name, String phoneNumber, String email, String deliveryAddress, String description, Uri imageUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.description = description;
        this.imageUri = imageUri;
    }
}
