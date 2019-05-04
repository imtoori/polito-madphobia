package com.mad.delivery.consumerApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public String name;
    public int quantity;
    public double price;


    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price=price;
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
        this.price=in.readDouble();
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
        parcel.writeDouble(price);
    }

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

