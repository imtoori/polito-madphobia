package com.mad.delivery.consumerApp;

import android.net.Uri;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String lastName;
    public String  phoneNumber;
    public String  email;
    public String  description;
    public String  road;
    public String  houseNumber;
    public String  doorPhone;
    public String  postCode;
    public String city;
    public Uri imageUri;
    public String registrationDate;

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
        registrationDate = new DateTime().toString();

    }

    public User() {}

}
