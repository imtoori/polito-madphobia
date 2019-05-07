package com.mad.delivery.resources;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    public String id;
    public Customer client;
    @Exclude
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


    public Order(Customer u, Restaurant r, List<Product> products, String orderFor, String paymentMethod) {
        this.client = u;
        this.products = products;
        status = OrderStatus.pending;
        orderDate = MyDateFormat.parse(new DateTime());
        this.restaurant = r;
        this.orderFor = orderFor;
        this.paymentMethod = paymentMethod;
        this.totalPrice = 0.0;
        products.forEach(p -> totalPrice += p.price);
    }

    public Order(Order other) {
        this.id = other.id;
        this.client = new Customer(other.client);
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
        this.clientId = other.clientId;
    }

    protected Order(Parcel in) {
        id = in.readString();
        client = in.readParcelable(Customer.class.getClassLoader());
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
        products = in.createTypedArrayList(Product.CREATOR);
        status = (OrderStatus) in.readSerializable();
        orderDate = in.readString();
        orderFor = in.readString();
        estimatedDelivery = in.readString();
        clientNotes = in.readString();
        serverNotes = in.readString();
        restaurantId = in.readString();
        paymentMethod = in.readString();
        totalPrice = in.readDouble();
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
        dest.writeString(restaurantId);
        dest.writeString(paymentMethod);
        dest.writeDouble(totalPrice);
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

    public Customer getClient() {
        return client;
    }

    public void setClient(Customer client) {
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
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
                ", restaurantId='" + restaurantId + '\'' +
                '}';
    }
}
