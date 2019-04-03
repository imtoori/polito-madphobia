package com.mad.delivery.restaurantApp;

import android.net.Uri;
import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String lastName;
    protected String  phoneNumber;
    protected String  email;
    protected String  description;
    protected String  road;
    protected String  houseNumber;
    protected String  doorPhone;
    protected String  postCode;
    protected String city;
    protected Uri imageUri;

    public User(String name, String lastName, String phoneNumber, String emailAddress, String description, String road, String houseNumber, String doorPhone, String postCode, String city, Uri imageUri) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.imageUri = imageUri;
    }

    public User() {}

}
