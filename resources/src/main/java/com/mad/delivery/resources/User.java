package com.mad.delivery.resources;

import android.net.Uri;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String lastname;
    public String phoneNumber;
    public String email;
    public String description;
    public Uri imageUri;
    public String registrationDate;

    public User(String name, String lastname, String phoneNumber, String emailAddress, String description, Uri imageUri) {
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.imageUri = imageUri;
        registrationDate = new DateTime().toString();

    }

    public User() {
    }

}