package com.mad.delivery.resources;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Biker implements Serializable {
    public String name;
    public String lastname;
    public String phoneNumber;
    public String email;
    public String description;
    public String registrationDate;
    public String imageName;
    public String id;
    public Boolean status;
    public Double latitude;
    public Double longitude;
    public Boolean visible;

    public Biker(String name, String lastname, String phoneNumber, String emailAddress, String description) {
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.registrationDate = new DateTime().toString();
        this.imageName  = "";
        this.status = false;
        this.visible = false;
        this.latitude = 0.0;
        this.longitude = 0.0;

    }

    public Biker() {
    }

    public Biker(Biker u) {
        this.id = u.id;
        this.registrationDate = u.registrationDate;
        this.name = u.name;
        this.lastname = u.lastname;
        this.phoneNumber = u.phoneNumber;
        this.email = u.email;
        this.description = u.description;
        this.imageName = u.imageName;
        this.status = u.status;
        this.visible = u.visible;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Biker{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", imageName='" + imageName + '\'' +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", visible=" + visible +
                '}';
    }

    public boolean isProfileComplete() {
        if(id == null || id.equals("")) {
            return false;
        }
        if(name== null || name.equals("")) {
            return false;
        }
        if(lastname== null || lastname.equals("")) {
            return false;
        }

        if(phoneNumber == null || phoneNumber.equals("")) return false;
        if(email == null || email.equals("")) return false;

        return true;
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
