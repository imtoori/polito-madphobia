package com.mad.delivery.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRest implements Parcelable {
    public String name, description;
    public Double price;
    public Integer ttl;
    public String imgUrl;
    public String category;
    public String id;
    public String restaurantId;

    public String imageDownload;
    public Integer availability;
    public String imageName;
    protected String imageUri;
    public List<String> subItems = new ArrayList<>();

    public MenuItemRest(String name, String category, String description, Double price, Integer availability, Integer ttl, String imgUrl, String  id, Uri url, List<String> subItems,String imageName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ttl = ttl;
        this.imgUrl = imgUrl;
        this.id = id;
        this.category = category;
        this.availability = availability;
        this.imageUri = url.toString();
        this.subItems = subItems;
        this.imageName =imageName;
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
        if (in.readByte() == 0) {
            ttl = null;
        } else {
            ttl = in.readInt();
        }
        imgUrl = in.readString();
        category = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readString();
        }
        if (in.readByte() == 0) {
            availability = null;
        } else {
            availability = in.readInt();
        }
        imageUri = in.readParcelable(Uri.class.getClassLoader());
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
        if (ttl == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ttl);
        }
        dest.writeString(imgUrl);
        dest.writeString(category);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(id);
        }
        if (availability == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(availability);
        }
        dest.writeParcelable(Uri.parse(imageUri), flags);
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

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public List<String> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<String> subItems) {
        this.subItems = subItems;
    }
}