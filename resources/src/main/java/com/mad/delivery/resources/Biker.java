package com.mad.delivery.resources;

import android.net.Uri;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Biker implements Serializable {
    public String name;
    public String lastname;
    public String phoneNumber;
    public String email;
    public String description;
    public String imageUri;
    public String registrationDate;
    public String imageName;
    public String id;
    public String imageDownload;
    public Boolean status;
    public Double latitude;
    public Double longitude;

    public Biker(String name, String lastname, String phoneNumber, String emailAddress, String description,  Uri imageUri) {
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.imageUri = imageUri.toString();
        this.registrationDate = new DateTime().toString();
        this.imageName  = imageUri.getLastPathSegment();
        this.status = false;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Biker() {
    }

    public Biker(Biker u) {
        this.name = u.name;
        this.lastname = u.lastname;
        this.phoneNumber = u.phoneNumber;
        this.email = u.email;
        this.description = u.description;
        this.imageUri = u.imageUri;
        this.imageName = u.imageName;
        this.latitude = u.latitude;
        this.longitude = u.latitude;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
