package com.mad.delivery.resources;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Feedback implements Serializable {
    public String id;
    public String clientID;
    public String restaurantID;
    public String orderID;
    public String date;
    public Double foodVote;
    public Double bikerVote;
    public Double serviceRestaurantVote;
    public String restaurantFeedback;
    public String bikerFeedback;

    public Feedback() {}
    public Feedback(String clientID, String restaurantID, String orderID, Double foodVote, Double bikerVote, Double serviceRestaurantVote) {
        this.clientID = clientID;
        this.restaurantID = restaurantID;
        this.orderID = orderID;
        this.foodVote = foodVote;
        this.bikerVote = bikerVote;
        this.serviceRestaurantVote = serviceRestaurantVote;
        this.date = new DateTime().toString();
        this.id = "";
        this.restaurantFeedback = "";
        this.bikerFeedback = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getFoodVote() {
        return foodVote;
    }

    public void setFoodVote(Double foodVote) {
        this.foodVote = foodVote;
    }

    public Double getBikerVote() {
        return bikerVote;
    }

    public void setBikerVote(Double bikerVote) {
        this.bikerVote = bikerVote;
    }

    public Double getServiceRestaurantVote() {
        return serviceRestaurantVote;
    }

    public void setServiceRestaurantVote(Double serviceRestaurantVote) {
        this.serviceRestaurantVote = serviceRestaurantVote;
    }

    public String getRestaurantFeedback() {
        return restaurantFeedback;
    }

    public void setRestaurantFeedback(String restaurantFeedback) {
        this.restaurantFeedback = restaurantFeedback;
    }

    public String getBikerFeedback() {
        return bikerFeedback;
    }

    public void setBikerFeedback(String bikerFeedback) {
        this.bikerFeedback = bikerFeedback;
    }
}
