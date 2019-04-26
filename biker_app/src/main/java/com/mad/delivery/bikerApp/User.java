package com.mad.delivery.bikerApp;

import android.net.Uri;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String lastName;
    protected String  phoneNumber;
    protected String  email;
    protected String  description;
    protected Uri imageUri;
    protected String registrationDate;

    public User(String name, String lastName, String phoneNumber, String emailAddress, String description, Uri imageUri) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.imageUri = imageUri;
        registrationDate = new DateTime().toString();
    }

    public User() {}

}
