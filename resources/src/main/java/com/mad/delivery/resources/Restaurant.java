package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant implements Parcelable {
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

    public Restaurant() {}
    public Restaurant(String name, String emailAddress, String description, String phoneNumber, String road, String houseNumber, String doorPhone, String postCode, String city, String imageUri, String imageName, String openingTime) {
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
        previewInfo.id = "";
        previewInfo.name = name;
        previewInfo.description =description;
        previewInfo.scoreValue = 0;
        previewInfo.imageName = "";
        previewInfo.deliveryCost = 0.0;
        previewInfo.minOrderCost = 0.0;
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
        this.categories = new HashMap<>();
        this.menuItems = new HashMap<>();
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
        int size1 = in.readInt();
        for(int i = 0; i < size1; i++){
            String key = in.readString();
            Boolean value =  (Boolean) in.readSerializable();
            categories.put(key,value);
        }
        int size2 = in.readInt();
        for(int i = 0; i < size2; i++){
            String key = in.readString();
            MenuItemRest value =  (MenuItemRest) in.readSerializable();
            menuItems.put(key,value);
        }
        token = in.readString();
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
        if(categories == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(categories.size());
            for(Map.Entry<String,Boolean> entry : categories.entrySet()){
                dest.writeString(entry.getKey());
                dest.writeSerializable(entry.getValue());
            }
        }

        if(menuItems == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(menuItems.size());
            for(Map.Entry<String,MenuItemRest> entry : menuItems.entrySet()){
                dest.writeString(entry.getKey());
                dest.writeSerializable(entry.getValue());
            }
        }

        dest.writeString(token);
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
}
