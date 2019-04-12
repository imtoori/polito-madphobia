package com.mad.delivery.restaurant_app;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.bloco.faker.Faker;

public final class Database {
    private static Database instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;
    private Database() {
        myDateComparator = new MyDateComparator();
        int randForProducts;
        orders = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Faker fakeProd = new Faker();
            List<Product> products = new ArrayList<>();
            randForProducts = r.nextInt(10) + 1;
            // create products for an order
            for (int j = 0; j < randForProducts; j++) {
                Product p = new Product(fakeProd.commerce.productName(), fakeProd.number.between(1, 20));
                products.add(p);

            }
            Customer u = new Customer();
            u.name = fakeProd.name.firstName();
            u.phoneNumber = fakeProd.phoneNumber.phoneNumber();
            u.description = fakeProd.lorem.paragraph(1);
            u.lastName = fakeProd.name.lastName();
            u.email = fakeProd.internet.email();
            u.city = fakeProd.address.city();
            u.road = fakeProd.address.streetName();
            u.houseNumber = fakeProd.address.buildingNumber();
            u.postCode = fakeProd.address.postcode();
            u.doorPhone = u.name;
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy hh:mm");
            DateTime from = new DateTime(2019, 3, 1, 19, 20, 30);
            DateTime to = DateTime.now();
            Order o = new Order(u, products, new DateTime(fakeProd.date.between(from.toDate(), to.toDate())));
            o.id = fakeProd.number.number(7).toString();
            o.status = OrderStatus.pending;
            o.orderDate = new DateTime(fakeProd.date.backward());
            o.estimatedDelivery = new DateTime(fakeProd.date.forward(1));
            o.clientNotes = fakeProd.lorem.paragraph(1);
            orders.put(o.id, o);

            Log.d("MADAPP", "Order generated : " + dtf.print(o.orderFor));
        }
    }

    public static void update(Order o) {
        Order old = orders.get(o.id);
        if(old != null) {
            Log.d("MADAPP", "Order with ID " + o.id + " has been updated.");
            old.update(o);
        }
    }

    class MyDateComparator implements Comparator<Order> {
        @Override
        public int compare(Order first, Order second) {
            return DateTimeComparator.getInstance().compare(second.orderFor, first.orderFor);
        }
    }


    public static List<Order> getPendingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> pendings = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders()");
        return pendings;
    }

    public static List<Order> getPreparingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> preparing = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.preparing) || o.status.equals(OrderStatus.ready)) preparing.add(o);
        }
        Collections.sort(preparing, myDateComparator);
        Log.d("MADAPP", "requested getPreparingOrders()");
        return preparing;
    }

    public static List<Order> getCompletedOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> completed = new ArrayList<>();
        for(Order o : orders.values()) {
            if(o.status.equals(OrderStatus.completed) || o.status.equals(OrderStatus.canceled)) completed.add(o);
        }
        Collections.sort(completed, myDateComparator);
        Log.d("MADAPP", "requested getCompletedOrders(), size is "  + completed.size());
        return completed;
    }

}

