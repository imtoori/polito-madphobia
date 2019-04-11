package com.mad.delivery.restaurant_app;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.bloco.faker.Faker;

final class Database {
    private static Database instance;
    private static List<Order> orders;
    private static List<MenuItem> menuItems;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        int randForProducts;
        orders = new ArrayList<>();
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
            Date from = new Date(119, 3, 1, 19, 20, 30);
            Date to = new Date();
            Order o = new Order(u, products, fakeProd.date.between(from, to));
            o.id = fakeProd.number.number(7).toString();
            o.status = OrderStatus.PENDING;
            o.orderDate = fakeProd.date.backward();
            o.estimatedDelivery = fakeProd.date.forward(1);
            o.clientNotes = fakeProd.lorem.paragraph(1);
            orders.add(o);

            Log.d("MADAPP", "Orders generated." + from.toString());
        }

        menuItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Faker faker = new Faker();
            MenuItem menuItem = new MenuItem(faker.name.name(), faker.lorem.sentence(), faker.commerce.price(), faker.number.between(10, 40), faker.avatar.image());
            menuItems.add(menuItem);

            Log.d("MADAPP", "Menu items generated.");
        }
    }


    public List<Order> getPendingOrders() {
        return orders;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}

