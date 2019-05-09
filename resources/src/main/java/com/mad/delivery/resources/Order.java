package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    public String id;
    public User client;
    public Restaurant restaurant;
    public List<Product> products;
    public OrderStatus status;
    public String orderDate;
    public String orderFor;
    public String estimatedDelivery;
    public String clientNotes;
    public String serverNotes;
    public String paymentMethod;
    public Double totalPrice;
    public String restaurantId;
    public String bikerId;
    public String clientId;

    public Order(){}

    public Order(User u, Restaurant r, List<Product> products, String orderFor, String paymentMethod) {
        this.client = u;
        this.products = products;
        status = OrderStatus.pending;
        orderDate = new DateTime().toString();
        this.restaurant = r;
        this.orderFor = orderFor;
        this.paymentMethod = paymentMethod;
//        products.forEach(p->Log.d("MADD",p.name+" "+p.quantity + " " + p.price));
        this.totalPrice=0.0;
        products.forEach(p->this.totalPrice+=p.price*p.quantity);

    }

    public Order(Order other) {
        this.id = other.id;
        this.client = new User(other.client);
        this.products = new ArrayList<>(other.products);
        this.restaurant = other.restaurant;
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        serverNotes = other.serverNotes;
        paymentMethod = other.paymentMethod;
        this.totalPrice = other.totalPrice;
    }


    protected Order(Parcel in) {
        id = in.readString();
        client = in.readParcelable(User.class.getClassLoader());
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
        products = in.createTypedArrayList(Product.CREATOR);
        status = (OrderStatus) in.readSerializable();
        orderDate = in.readString();
        orderFor = in.readString();
        estimatedDelivery = in.readString();
        clientNotes = in.readString();
        serverNotes = in.readString();
        paymentMethod = in.readString();
        if (in.readByte() == 0) {
            totalPrice = null;
        } else {
            totalPrice = in.readDouble();
        }
        restaurantId = in.readString();
        bikerId = in.readString();
        clientId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(client, flags);
        dest.writeParcelable(restaurant, flags);
        dest.writeTypedList(products);
        dest.writeSerializable(status);
        dest.writeString(orderDate);
        dest.writeString(orderFor);
        dest.writeString(estimatedDelivery);
        dest.writeString(clientNotes);
        dest.writeString(serverNotes);
        dest.writeString(paymentMethod);
        if (totalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(totalPrice);
        }
        dest.writeString(restaurantId);
        dest.writeString(bikerId);
        dest.writeString(clientId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public void addProduct(Product p) {
        if(products.contains(p)) {
            Product product = products.get(products.indexOf(p));
            product.setQuantity(product.quantity + p.quantity);
        } else {
            products.add(p);
        }
    }
    public void remProduct(Product p) {
        if(products.contains(p)) {
            Product oldProduct = products.get(products.indexOf(p));
            if(oldProduct.quantity <= p.quantity)
                products.remove(oldProduct);
             else
                oldProduct.quantity -= p.quantity;
        }
    }

    public void update(Order other) {

        this.id = other.id;
        this.client = other.client;
        this.products = other.products;
        this.restaurant = other.restaurant;
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        serverNotes = other.serverNotes;
        restaurantId = other.restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderFor() {
        return orderFor;
    }

    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    public String getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(String estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getClientNotes() {
        return clientNotes;
    }

    public void setClientNotes(String clientNotes) {
        this.clientNotes = clientNotes;
    }

    public String getServerNotes() {
        return serverNotes;
    }

    public void setServerNotes(String serverNotes) {
        this.serverNotes = serverNotes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getBikerId() {
        return bikerId;
    }

    public void setBikerId(String bikerId) {
        this.bikerId = bikerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", client=" + client +
                ", restaurant=" + restaurant +
                ", products=" + products +
                ", status=" + status +
                ", orderDate='" + orderDate + '\'' +
                ", orderFor='" + orderFor + '\'' +
                ", estimatedDelivery='" + estimatedDelivery + '\'' +
                ", clientNotes='" + clientNotes + '\'' +
                ", serverNotes='" + serverNotes + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalPrice=" + totalPrice +
                ", restaurantId='" + restaurantId + '\'' +
                ", bikerId='" + bikerId + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
