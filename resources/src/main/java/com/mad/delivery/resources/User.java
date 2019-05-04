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
    public String imageUri;
    public String registrationDate;
    public String  road;
    public String  houseNumber;
    public String  doorPhone;
    public String  postCode;
    public String city;
    public String imageName;
    public String opening;
    public String id;

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
        this.registrationDate = new DateTime().toString();
    }

    public User() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getDoorPhone() {
        return doorPhone;
    }

    public void setDoorPhone(String doorPhone) {
        this.doorPhone = doorPhone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}