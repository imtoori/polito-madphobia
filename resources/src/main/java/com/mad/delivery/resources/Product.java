package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public String name;
    public int quantity;
    public double price;

    public Product(String name, int quantity, double price) {

        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Product() {}

    protected Product(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

