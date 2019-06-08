package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

public class MenuOffer implements Parcelable {
    public String id;

    public List<MenuItemRest> items;
    public Double price;
    public String restaurantID;
    public String startDateTime;
    public String endDateTime;

    public MenuOffer() {}
    public MenuOffer(String restaurantID, List<MenuItemRest> items, Double price, Integer hours) {
        this.restaurantID = restaurantID;
        this.items = items;
        this.price = price;
        DateTime now = new DateTime();
        this.endDateTime = now.plusHours(hours).toString();
        this.startDateTime = now.toString();
        this.id = "";
    }

    protected MenuOffer(Parcel in) {
        id = in.readString();
        items = in.readArrayList(MenuItemRest.class.getClassLoader());
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        restaurantID = in.readString();
        startDateTime = in.readString();
        endDateTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeList(items);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeString(restaurantID);
        dest.writeString(startDateTime);
        dest.writeString(endDateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuOffer> CREATOR = new Creator<MenuOffer>() {
        @Override
        public MenuOffer createFromParcel(Parcel in) {
            return new MenuOffer(in);
        }

        @Override
        public MenuOffer[] newArray(int size) {
            return new MenuOffer[size];
        }
    };

    public List<MenuItemRest> getItems() {
        return items;
    }

    public void setItems(List<MenuItemRest> items) {
        this.items = items;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public static Creator<MenuOffer> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
