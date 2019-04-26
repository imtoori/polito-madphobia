package com.mad.delivery.bikerApp;

import android.os.Parcel;
import android.os.Parcelable;

class Product implements Parcelable {
    protected String name;
    protected int quantity;


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

