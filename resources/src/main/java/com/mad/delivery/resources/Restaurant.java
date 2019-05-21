package com.mad.delivery.resources;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
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
    public Map<String, MenuItemRest> menu;
    public Map<String, MenuOffer> offers;
    public String token;
    public Double latitude;
    public Double longitude;
    public Boolean visible;

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
        this.menu = new HashMap<>();
        this.offers = new HashMap<>();
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
        this.visible = false;
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
        this.offers = other.offers;
        this.menu = other.menu;
        this.token = other.token;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.visible = other.visible;
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
        token = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        byte tmpVisible = in.readByte();
        visible = tmpVisible == 0 ? null : tmpVisible == 1;
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
        dest.writeByte((byte) (visible == null ? 0 : visible ? 1 : 2));
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
    public Map<String, MenuItemRest> getMenu() {
        return menu;
    }
    public void setMenu(Map<String, MenuItemRest> menu) {
        this.menu = menu;
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

    public Map<String, MenuOffer> getOffers() {
        return offers;
    }

    public void setOffers(Map<String, MenuOffer> offers) {
        this.offers = offers;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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
                ", menu=" + menu +
                ", token='" + token + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public boolean isProfileComplete() {
        if(previewInfo == null) {
            Log.d("MADAPP", "previewinfo");
            return false;
        }
        if(previewInfo.id == null || previewInfo.minOrderCost == null || previewInfo.deliveryCost == null || previewInfo.name == null || previewInfo.description == null) {
            Log.d("MADAPP", "previewinfo2 " );
            return false;
        }
        if(previewInfo.id.equals("") || previewInfo.name.equals("") || previewInfo.description.equals("")) {
            Log.d("MADAPP", "previewinfo3");
            return false;
        }
        if(phoneNumber == null || phoneNumber.equals("")) return false;
        if(email == null || email.equals("")) return false;
        if(road == null || road.equals("")) return false;
        if(houseNumber == null || houseNumber.equals("")) return false;
        if(doorPhone == null || doorPhone.equals("")) return false;
        if(postCode == null || postCode.equals("")) return false;
        if(city == null || city.equals("")) return false;
        if(openingHours == null || openingHours.equals("")) return false;
        if(categories == null || categories.size() == 0) return false;
        if(menu == null || menu.size() == 0) return false;

        return true;
    }
}
