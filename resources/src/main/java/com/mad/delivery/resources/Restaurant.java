package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant implements Serializable, Parcelable {
    public PreviewInfo previewInfo;
    public String phoneNumber;
    public String email;
    public String road;
    public String houseNumber;
    public String doorPhone;
    public String postCode;
    public String city;
    public String openingHours;
    public Map<String, Boolean> categories;
    public Map<String, MenuItemRest> menuItems;
    public String token;
    public Double latitude;
    public Double longitude;

    public Restaurant() {}
    public Restaurant(String id, String name, String emailAddress, String description, String phoneNumber, String road, String houseNumber, String doorPhone, String postCode, String city, String imageUri, String imageName, String openingTime) {
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.openingHours=openingTime;
        this.categories = new HashMap<>();
        this.menuItems = new HashMap<>();
        this.token ="";
        previewInfo = new PreviewInfo();
        previewInfo.id = id;
        previewInfo.name = name;
        previewInfo.description =description;
        previewInfo.scoreValue = 0;
        previewInfo.imageName = "";
        previewInfo.deliveryCost = 0.0;
        previewInfo.minOrderCost = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Restaurant(Restaurant other) {
        this.previewInfo = new PreviewInfo();
        this.previewInfo.id = other.previewInfo.id;
        this.previewInfo.name = other.previewInfo.name;
        this.previewInfo.description = other.previewInfo.description;
        this.previewInfo.imageName = other.previewInfo.imageName;
        this.previewInfo.scoreValue = other.previewInfo.scoreValue;
        this.previewInfo.deliveryCost = other.previewInfo.deliveryCost;
        this.previewInfo.minOrderCost = other.previewInfo.minOrderCost;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
        this.road = other.road;
        this.houseNumber = other.houseNumber;
        this.doorPhone = other.doorPhone;
        this.postCode = other.postCode;
        this.city = other.city;
        this.openingHours = other.openingHours;
        this.categories=other.categories;
        this.menuItems = other.menuItems;
        this.token = other.token;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
    }

    protected Restaurant(Parcel in) {
        previewInfo = in.readParcelable(PreviewInfo.class.getClassLoader());
        phoneNumber = in.readString();
        email = in.readString();
        road = in.readString();
        houseNumber = in.readString();
        doorPhone = in.readString();
        postCode = in.readString();
        city = in.readString();
        openingHours = in.readString();
        categories = new HashMap<>();
        in.readMap(categories,Boolean.class.getClassLoader());
        menuItems = new HashMap<>();
        in.readMap(menuItems, MenuItemRest.class.getClassLoader());
        token = in.readString();
        if(in.readByte() == 0) {
            latitude = 0.0;
        } else {
            latitude = in.readDouble();
        }
        if(in.readByte() == 0) {
            longitude = 0.0;
        } else {
            longitude = in.readDouble();
        }


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(previewInfo, flags);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(road);
        dest.writeString(houseNumber);
        dest.writeString(doorPhone);
        dest.writeString(postCode);
        dest.writeString(city);
        dest.writeString(openingHours);
        dest.writeMap(categories);
        dest.writeMap(menuItems);
        dest.writeString(token);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public PreviewInfo getPreviewInfo() {
        return previewInfo;
    }

    public void setPreviewInfo(PreviewInfo previewInfo) {
        this.previewInfo = previewInfo;
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
    public String getOpeningHours() {
        return openingHours;
    }
    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
    public Map<String, Boolean> getCategories() {
        return categories;
    }
    public void setCategories(Map<String, Boolean> categories) {
        this.categories = categories;
    }
    public Map<String, MenuItemRest> getMenuItems() {
        return menuItems;
    }
    public void setMenuItems(Map<String, MenuItemRest> menuItems) {
        this.menuItems = menuItems;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public static Creator<Restaurant> getCREATOR() {
        return CREATOR;
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "previewInfo=" + previewInfo +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", road='" + road + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", doorPhone='" + doorPhone + '\'' +
                ", postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                ", categories=" + categories +
                ", menuItems=" + menuItems +
                ", token='" + token + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
