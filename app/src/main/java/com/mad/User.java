package com.mad;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String description;
    public String emailAddress;
    public String deliveryAddress;
    public String imageSrc;

    public User(String name, String description, String emailAddress, String deliveryAddress, String imageSrc) {
        this.name = name;
        this.description = description;
        this.emailAddress = emailAddress;
        this.deliveryAddress = deliveryAddress;
        this.imageSrc = imageSrc;
    }



}
