package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant implements Parcelable {
    public PreviewInfo previewInfo;
    public String name;
    public String phoneNumber;
    public String registrationDate;
    public String email;
    public String description;
    public String road;
    public String houseNumber;
    public String doorPhone;
    public String postCode;
    public String city;
    public String openingHours;
    public String imageName;
    public String id;
    public Integer scoreValue;
    public Map<String, Boolean> categories;
    public Map<String, MenuItemRest> menuItems;
    public String imageUri;
    public String token;
    public Integer minOrderCost, deliveryCost;

    public Restaurant(String name,  String emailAddress, String description, String phoneNumber, String road, String houseNumber, String doorPhone, String postCode, String city, String imageUri,String imageName,String openingTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.imageUri = imageUri;
        this.imageName =imageName;
        this.openingHours = openingTime;
        categories = new HashMap<>();
        menuItems = new HashMap<>();
        previewInfo = new PreviewInfo();
        previewInfo.name = this.name;
        previewInfo.scoreValue = 0;
        previewInfo.imageURL = "";
        previewInfo.description = this.description;
    }

    public Restaurant(Restaurant other) {
        this.name = other.name;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
        this.description = other.description;
        this.road = other.road;
        this.houseNumber = other.houseNumber;
        this.doorPhone = other.doorPhone;
        this.postCode = other.postCode;
        this.city = other.city;
        this.imageUri = other.imageUri;
        this.openingHours = other.openingHours;
        previewInfo = new PreviewInfo();
        previewInfo.name = this.name;
        previewInfo.scoreValue = 0;
        previewInfo.imageURL = "";
        previewInfo.description = this.description;
    }


    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant() {
        this.scoreValue = 0;
    }

    protected Restaurant(Parcel in) {
        previewInfo = in.readParcelable(PreviewInfo.class.getClassLoader());
        name = in.readString();
        phoneNumber = in.readString();
        registrationDate = in.readString();
        email = in.readString();
        description = in.readString();
        road = in.readString();
        houseNumber = in.readString();
        doorPhone = in.readString();
        postCode = in.readString();
        city = in.readString();
        openingHours = in.readString();
        scoreValue = in.readInt();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(previewInfo, flags);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(registrationDate);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(road);
        dest.writeString(houseNumber);
        dest.writeString(doorPhone);
        dest.writeString(postCode);
        dest.writeString(city);
        dest.writeString(openingHours);
        dest.writeInt(scoreValue);
        dest.writeString(imageUri);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Map<String, Boolean> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Boolean> categories) {
        this.categories = categories;
    }

    public Map<String ,MenuItemRest> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Map<String, MenuItemRest> menuItems) {
        this.menuItems = menuItems;
    }

    public Integer getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PreviewInfo getPreviewInfo() {
        return previewInfo;
    }

    public void setPreviewInfo(PreviewInfo previewInfo) {
        this.previewInfo = previewInfo;
    }

    public Integer getMinOrderCost() {
        return minOrderCost;
    }

    public void setMinOrderCost(int minOrderCost) {
        this.minOrderCost = minOrderCost;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "previewInfo=" + previewInfo +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", road='" + road + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", doorPhone='" + doorPhone + '\'' +
                ", postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", scoreValue=" + scoreValue +
                ", categories=" + categories +
                ", menuItems=" + menuItems +
                ", imageUri=" + imageUri +
                ", token='" + token + '\'' +
                ", minOrderCost=" + minOrderCost +
                ", deliveryCost=" + deliveryCost +
                '}';
    }
}
