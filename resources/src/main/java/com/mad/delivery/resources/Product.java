package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Product implements Serializable, Parcelable {
    public String name;
    public Integer quantity;
    public double price;
    public String idItem;

    public Product(String name, Integer quantity, double price,String idItem) {

        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.idItem = idItem;
    }

    public Product(MenuItemRest item, Integer quantity) {
        this.name = item.name;
        this.idItem = item.id;
        this.quantity = quantity;
        this.price = (Double) item.price * quantity;
    }



    public Product() {}

    protected Product(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        idItem = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeString(idItem);
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
    public void addProduct(MenuItemRest item, Integer q) {
        this.quantity += q;
        this.price += item.price * q;
    }

    public void delProduct(MenuItemRest item, Integer q) {
        this.quantity -= q;
        this.price -= item.price * q;
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

