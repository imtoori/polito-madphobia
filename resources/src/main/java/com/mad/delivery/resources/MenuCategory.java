package com.mad.delivery.resources;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MenuCategory implements Parcelable {
    public String name;
    public List<MenuItemRest> items;

    public MenuCategory() {}
    public MenuCategory(String name, List<MenuItemRest> items) {
        this.name = name;
        this.items = items;
    }

    protected MenuCategory(Parcel in) {
        name = in.readString();
        items = in.createTypedArrayList(MenuItemRest.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuCategory> CREATOR = new Creator<MenuCategory>() {
        @Override
        public MenuCategory createFromParcel(Parcel in) {
            return new MenuCategory(in);
        }

        @Override
        public MenuCategory[] newArray(int size) {
            return new MenuCategory[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItemRest> getItems() {
        return items;
    }

    public void setItems(List<MenuItemRest> items) {
        this.items = items;
    }
}
