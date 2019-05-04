package com.mad.delivery.consumerApp;

import android.os.Parcel;
import android.os.Parcelable;

import com.mad.delivery.consumerApp.Product;
import com.mad.delivery.consumerApp.Restaurant;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    public Restaurant restaurant;
    public String id;
    public List<Product> products;
    public OrderStatus status;
    public DateTime orderDate;
    public DateTime orderFor;
    public DateTime estimatedDelivery;
    public String clientNotes;
    public String serverNotes;
    public String restNotes;
    public Double price;
    public Boolean method; //t=credit; f=cash

    public Order(Restaurant r, List<Product> products, DateTime orderFor, Double price, Boolean method) {
        this.restaurant=r;
        this.products = products;
        this.price=price;
        this.method=method;
        status = OrderStatus.pending;
        orderDate = new DateTime();
        this.orderFor = orderFor;
    }


    public Order(Order other) {
        this.id = other.id;
        this.restaurant=new Restaurant(other.restaurant);
        this.products = new ArrayList<>(other.products);
        this.price=other.price;
        this.method=other.method;
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
        price=in.readDouble();
        method=(Boolean) in.readSerializable();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeValue(restaurant);
        parcel.writeTypedList(products);
        parcel.writeSerializable(status);
        parcel.writeSerializable(orderDate);
        parcel.writeSerializable(orderFor);
        parcel.writeSerializable(estimatedDelivery);
        parcel.writeString(clientNotes);
        parcel.writeString(serverNotes);
        parcel.writeString(restNotes);
        parcel.writeDouble(price);
        parcel.writeSerializable(method);
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
        this.restaurant= other.restaurant;
        this.products = other.products;
        status = other.status;
        orderDate = other.orderDate;
        orderFor = other.orderFor;
        estimatedDelivery = other.estimatedDelivery;
        clientNotes = other.clientNotes;
        restNotes= other.restNotes;
        serverNotes = other.serverNotes;
        price=other.price;
        method=other.method;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", restaurant=" + restaurant +
                ", products=" + products +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", orderFor=" + orderFor +
                ", estimatedDelivery=" + estimatedDelivery +
                ", clientNotes='" + clientNotes + '\'' +
                ", serverNotes='" + serverNotes + '\'' +
                ", price= "+ price + "€"+
                '}';
    }
}
