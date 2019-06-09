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
    public Double score;
    public int scoreCount;
   // public Integer order_count;
   // public Double km;
    //public Double earning;
    //public Double hours;

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
        //this.order_count=0;
        //this.km=0.00;
        //this.earning=0.00;
        //this.hours=0.00;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.score = 0.0;
        this.scoreCount = 0;

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
        //this.order_count= u.order_count;
        //this.km=u.km;
        //this.earning=u.earning;
        //this.hours=u.hours;
        this.latitude = u.latitude;
        this.longitude = u.latitude;
        this.score = u.score;
        this.scoreCount = u.scoreCount;
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

    /*public Integer getOrderCount() {
        return order_count;
    }

    public void setOrderCount(Integer order_count) { this.order_count = order_count; }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Double getEarning() {
        return earning;
    }

    public void setEarning(Double earning) {
        this.earning = earning;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
*/
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }
}
