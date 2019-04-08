package com.mad.delivery.restaurant_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Order  implements Parcelable {
    protected String id;
    protected Customer client;
    protected List<Product> products;
    protected OrderStatus status;
    protected Date orderDate;
    protected Date orderFor;
    protected Date estimatedDelivery;
    protected String clientNotes;
    protected String serverNotes;

    public Order(Customer u, List<Product> products, Date orderFor) {
        this.client = u;
        this.products = products;
        status = OrderStatus.PENDING;
        orderDate = new Date();
        this.orderFor = orderFor;
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
        orderDate = (Date) in.readSerializable();
        orderFor = (Date) in.readSerializable();
        estimatedDelivery = (Date) in.readSerializable();
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
        parcel.writeSerializable(orderDate);
        parcel.writeSerializable(orderFor);
        parcel.writeSerializable(estimatedDelivery);
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


}
