package com.mad.delivery.biker_app;

import android.net.Uri;
import java.io.Serializable;

public class User implements Serializable {
    protected String name;
    protected String lastname;
    protected String  phoneNumber;
    protected String  email;
    protected String  description;
    protected Uri imageUri;

    public User(String name, String lastname ,String phoneNumber, String emailAddress, String description, Uri imageUri) {
        this.name = name;
        this.lastname= lastname;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;

        this.imageUri = imageUri;

    }

    public User() {}

}