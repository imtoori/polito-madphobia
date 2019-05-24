package com.mad.delivery.resources;

import java.util.Comparator;

public class SortByClosestDistance implements Comparator<DistanceBiker> {

    @Override
    public int compare(DistanceBiker a, DistanceBiker b) {
        return Double.compare(a.distance, b.distance);
    }
}