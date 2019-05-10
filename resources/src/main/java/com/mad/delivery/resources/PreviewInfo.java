package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class PreviewInfo implements Parcelable {
    public String id;
    public String name;
    public String description;
    public Integer scoreValue;
    public String imageURL;
    public String imageDownload;

    public Double deliveryCost;
    public Double minOrderCost;

    public PreviewInfo() {}

    protected PreviewInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            scoreValue = null;
        } else {
            scoreValue = in.readInt();
        }
        imageURL = in.readString();
        imageDownload = in.readString();
        deliveryCost = in.readDouble();
        minOrderCost = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        if (scoreValue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(scoreValue);
        }
        dest.writeString(imageURL);
        dest.writeString(imageDownload);
        if(deliveryCost==null)
            dest.writeDouble(0.0);
        else
            dest.writeDouble(deliveryCost);
        if(minOrderCost==null)
            dest.writeDouble(0.0);
        else
            dest.writeDouble(minOrderCost);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreviewInfo> CREATOR = new Creator<PreviewInfo>() {
        @Override
        public PreviewInfo createFromParcel(Parcel in) {
            return new PreviewInfo(in);
        }

        @Override
        public PreviewInfo[] newArray(int size) {
            return new PreviewInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Integer scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageDownload() {
        return imageDownload;
    }

    public void setImageDownload(String imageDownload) {
        this.imageDownload = imageDownload;
    }

    public Double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Double getMinOrderCost() {
        return minOrderCost;
    }

    public void setMinOrderCost(Double minOrderCost) {
        this.minOrderCost = minOrderCost;
    }

    public static Creator<PreviewInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "PreviewInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scoreValue=" + scoreValue +
                ", imageURL='" + imageURL + '\'' +
                ", imageDownload='" + imageDownload + '\'' +
                ", deliveryCost=" + deliveryCost +
                ", minOrderCost=" + minOrderCost +
                '}';
    }
}
