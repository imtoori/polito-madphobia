package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class PreviewInfo implements Parcelable {
    public String id;
    public String name;
    public String description;
    public Double scoreValue;
    public String imageName;
    public Double deliveryCost;
    public Double minOrderCost;
    public Integer scoreCount;

    public PreviewInfo() {}


    protected PreviewInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            scoreValue = null;
        } else {
            scoreValue = in.readDouble();
        }
        imageName = in.readString();
        if (in.readByte() == 0) {
            deliveryCost = null;
        } else {
            deliveryCost = in.readDouble();
        }
        if (in.readByte() == 0) {
            minOrderCost = null;
        } else {
            minOrderCost = in.readDouble();
        }
        if (in.readByte() == 0) {
            scoreCount = null;
        } else {
            scoreCount = in.readInt();
        }
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
            dest.writeDouble(scoreValue);
        }
        dest.writeString(imageName);
        if (deliveryCost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(deliveryCost);
        }
        if (minOrderCost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minOrderCost);
        }
        if (scoreCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(scoreCount);
        }
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

    public Double getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Double scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public Integer getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Integer scoreCount) {
        this.scoreCount = scoreCount;
    }
}
