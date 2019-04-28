package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
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
    public Uri imageUri;

    public Restaurant(String name,  String emailAddress, String description, String phoneNumber, String road, String houseNumber, String doorPhone, String postCode, String city, Uri imageUri) {
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
    }

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
        parcel.writeString(email);
        parcel.writeString(description);
        parcel.writeString(road);
        parcel.writeString(houseNumber);
        parcel.writeString(doorPhone);
        parcel.writeString(postCode);
        parcel.writeString(city);
        parcel.writeParcelable(imageUri, i);
    }

    public Restaurant(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        description = in.readString();
        road = in.readString();
        houseNumber = in.readString();
        doorPhone = in.readString();
        postCode = in.readString();
        city = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());

    }

    public static final Creator<Restaurant> CREATOR
            = new Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}

