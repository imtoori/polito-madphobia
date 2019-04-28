package com.mad.delivery.restaurant_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Order  implements Parcelable {
    protected String id;
    protected Customer client;
    protected List<Product> products;
    protected OrderStatus status;
    protected String orderDate;
    protected String orderFor;
    protected String estimatedDelivery;
    protected String clientNotes;
    protected String serverNotes;
    public Order(){}
    public Order(Customer u, List<Product> products, String orderFor) {
        this.client = u;
        this.products = products;
        status = OrderStatus.pending;
        orderDate = new String();
        this.orderFor = orderFor;
    }

    public Order(Order other) {
        this.id = other.id;
        this.client = new Customer(other.client);
        this.products = new ArrayList<>(other.products);
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
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
            products = new ArrayList<Product>();
            in.readTypedList(products, Product.CREATOR);
        status = (OrderStatus) in.readSerializable();
        orderDate =  in.readString();
        orderFor = in.readString();
        estimatedDelivery = in.readString();
        clientNotes = in.readString();
        serverNotes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(client, i);
        parcel.writeTypedList(products);
        parcel.writeSerializable(status);
        parcel.writeString(orderDate);
        parcel.writeString(orderFor);
        parcel.writeString(estimatedDelivery);
        parcel.writeString(clientNotes);
        parcel.writeString(serverNotes);
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
        this.products = other.products;
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        serverNotes = other.serverNotes;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", client=" + client +
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
