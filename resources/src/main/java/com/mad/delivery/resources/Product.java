package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public String name;
    public int quantity;


    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public Product(Parcel in) {
        this.name = in.readString();
        this.quantity = in.readInt();
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(quantity);
    }

    public static final Creator<Product> CREATOR
            = new Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

