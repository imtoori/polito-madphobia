package com.mad.delivery.restaurant_app;

import android.net.Uri;
import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String  phoneNumber;
    protected String  email;
    protected String  description;
    protected String  road;
    protected String  houseNumber;
    protected String  doorPhone;
    protected String  postCode;
    protected String city;
    protected String imageUri;
    protected String imageName;
    protected String opening;
    protected String id;

    public User(String name,String phoneNumber, String emailAddress, String description, String road, String houseNumber, String doorPhone, String postCode, String city, Uri imageUri, String opening,String imageName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.imageUri = imageUri.toString();
        this.opening = opening;
        this.imageName = imageName;
    }

    public User() {}
    public User(User u) {
        this.name = u.name;
        this.phoneNumber = u.phoneNumber;
        this.email = u.email;
        this.description = u.description;
        this.road = u.road;
        this.houseNumber = u.houseNumber;
        this.doorPhone = u.doorPhone;
        this.postCode = u.postCode;
        this.city = u.city;
        this.imageUri = u.imageUri;
        this.opening = u.opening;
        this.imageName = u.imageName;
    }

}