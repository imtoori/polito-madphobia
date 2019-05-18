package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRest implements Serializable, Parcelable {
    public String name, description;
    public Double price;
    public String category;
    public String id;
    public String restaurantId;
    public Integer availability;
    public String imageName;
    public List<String> subItems;

    public MenuItemRest(String name, String description, Double price, String category, String id, String restaurantId, Integer availability) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.id = id;
        this.restaurantId = restaurantId;
        this.availability = availability;
        subItems = new ArrayList<>();
    }

    public MenuItemRest() {
    }

    protected MenuItemRest(Parcel in) {
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        category = in.readString();
        id = in.readString();
        restaurantId = in.readString();
        if (in.readByte() == 0) {
            availability = null;
        } else {
            availability = in.readInt();
        }
        imageName = in.readString();
        subItems = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeString(category);
        dest.writeString(id);
        dest.writeString(restaurantId);
        if (availability == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(availability);
        }
        dest.writeString(imageName);
        dest.writeStringList(subItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuItemRest> CREATOR = new Creator<MenuItemRest>() {
        @Override
        public MenuItemRest createFromParcel(Parcel in) {
            return new MenuItemRest(in);
        }

        @Override
        public MenuItemRest[] newArray(int size) {
            return new MenuItemRest[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<String> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<String> subItems) {
        this.subItems = subItems;
    }

    public static Creator<MenuItemRest> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "MenuItemRest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", id='" + id + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", availability=" + availability +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}