package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    public String name;
    public String lastName;
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
    public String id;
    public String imageName;
    public Integer credit;

    public User(String name, String lastName, String phoneNumber, String emailAddress, String description, String road, String houseNumber, String doorPhone, String postCode, String city, Uri imageUri, String imageName) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.description = description;
        this.road = road;
        this.houseNumber = houseNumber;
        this.doorPhone = doorPhone;
        this.postCode = postCode;
        this.city = city;
        this.imageUri = imageUri.toString();
        this.registrationDate = new DateTime().toString();
        this.imageName = imageName;
        this.credit =0;
    }

    public User() {
    }

    public User(User u) {
        this.id = u.id;
        this.name = u.name;
        this.lastName = u.lastName;
        this.phoneNumber = u.phoneNumber;
        this.email = u.email;
        this.description = u.description;
        this.road = u.road;
        this.houseNumber = u.houseNumber;
        this.doorPhone = u.doorPhone;
        this.postCode = u.postCode;
        this.city = u.city;
        this.imageUri = u.imageUri;
        this.imageName = u.imageName;
    }


    protected User(Parcel in) {
        name = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        description = in.readString();
        imageUri = in.readString();
        registrationDate = in.readString();
        road = in.readString();
        houseNumber = in.readString();
        doorPhone = in.readString();
        postCode = in.readString();
        city = in.readString();
        id = in.readString();
        imageName = in.readString();
        if (in.readByte() == 0) {
            credit = null;
        } else {
            credit = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(imageUri);
        dest.writeString(registrationDate);
        dest.writeString(road);
        dest.writeString(houseNumber);
        dest.writeString(doorPhone);
        dest.writeString(postCode);
        dest.writeString(city);
        dest.writeString(id);
        dest.writeString(imageName);
        if (credit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(credit);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", road='" + road + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", doorPhone='" + doorPhone + '\'' +
                ", postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", imageName='" + imageName + '\'' +
                ", credit=" + credit +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }
}