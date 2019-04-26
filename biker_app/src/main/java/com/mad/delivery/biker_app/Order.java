package com.mad.delivery.biker_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Order implements Parcelable {
    public Restaurant restaurant;
    protected String id;
    protected Customer client;
    protected List<Product> products;
    protected OrderStatus status;
    protected DateTime orderDate;
    protected DateTime orderFor;
    protected DateTime estimatedDelivery;
    protected String clientNotes;
    protected String serverNotes;
    protected String restNotes;

    public Order(Customer u, Restaurant r, List<Product> products, DateTime orderFor) {
        this.client = u;
        this.restaurant=r;
        this.products = products;
        status = OrderStatus.pending;
        orderDate = new DateTime();
        this.orderFor = orderFor;
    }

    public Order(Order other) {
        this.id = other.id;
        this.client = new Customer(other.client);
        this.restaurant=new Restaurant(other.restaurant);
        this.products = new ArrayList<>(other.products);
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        restNotes=other.restNotes;
        serverNotes = other.serverNotes;
    }

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
    private Order(Parcel in) {
        id = in.readString();
        client = in.readParcelable(Customer.class.getClassLoader());
        restaurant= in.readParcelable(Restaurant.class.getClassLoader());
            products = new ArrayList<Product>();
            in.readTypedList(products, Product.CREATOR);
        status = (OrderStatus) in.readSerializable();
        orderDate = (DateTime) in.readSerializable();
        orderFor = (DateTime) in.readSerializable();
        estimatedDelivery = (DateTime) in.readSerializable();
        clientNotes = in.readString();
        serverNotes = in.readString();
        restNotes= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(client, i);
        parcel.writeParcelable(restaurant, i);
        parcel.writeTypedList(products);
        parcel.writeSerializable(status);
        parcel.writeSerializable(orderDate);
        parcel.writeSerializable(orderFor);
        parcel.writeSerializable(estimatedDelivery);
        parcel.writeString(clientNotes);
        parcel.writeString(serverNotes);
        parcel.writeString(restNotes);
    }

    public static final Parcelable.Creator<Order> CREATOR
            = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public void update(Order other) {
        this.id = other.id;
        this.client = other.client;
        this.restaurant= other.restaurant;
        this.products = other.products;
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        restNotes= other.restNotes;
        serverNotes = other.serverNotes;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", client=" + client +
                ", restaurant=" + restaurant +
                ", products=" + products +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", orderFor=" + orderFor +
                ", estimatedDelivery=" + estimatedDelivery +
                ", clientNotes='" + clientNotes + '\'' +
                ", serverNotes='" + serverNotes + '\'' +
                '}';
    }
}
