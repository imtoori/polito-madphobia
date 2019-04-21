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
    protected Uri imageUri;
    protected String opening;

    public User(String name,String phoneNumber, String emailAddress, String description, String road, String houseNumber, String doorPhone, String postCode, String city, Uri imageUri, String opening) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.imageUri = imageUri;
        this.opening = opening;
    }

    public User() {}

}