package com.mad.delivery.resources;

import com.mad.delivery.resources.Biker;

import java.util.Comparator;

public class DistanceBiker {
    public Biker biker;
    public Double distance;

    public DistanceBiker(Biker biker, Double distance) {
        this.biker = biker;
        this.distance = distance;
    }

    public Biker getBiker() {
        return biker;
    }

    public void setBiker(Biker biker) {
        this.biker = biker;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}


