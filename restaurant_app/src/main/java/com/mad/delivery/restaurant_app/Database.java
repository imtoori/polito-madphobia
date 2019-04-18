package com.mad.delivery.restaurant_app;

import android.net.Uri;
import android.util.Log;
import android.view.Menu;

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


final class Database {
    private static Database instance;
    private static Map<String, Order> orders;
    private static MyDateComparator myDateComparator;
    private static List<MenuItemRest> menuItems;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        myDateComparator = new MyDateComparator();
        orders = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            List<Product> products = new ArrayList<>();
            // create products for an order
            Product p1 = new Product("Prodotto 1", 20);
            Product p2 = new Product("Prodotto 2", 10);
            products.add(p1);
            products.add(p2);

            Customer u = new Customer();
            u.name = "FirstName";
            u.phoneNumber = "+393926282813";
            u.description = "This is a description";
            u.lastName = "Last Name";
            u.email = "email@email.it";
            u.city = "Turin";
            u.road = "Street name";
            u.houseNumber = "20";
            u.postCode = "10126";
            u.doorPhone = "doorphone";
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy HH:mm");
            DateTime from = new DateTime(2019, 3, 1, 19, 20, 30);
            DateTime to = DateTime.now();
            Order o = new Order(u, products, from);
            o.id = "1234";
            o.status = OrderStatus.pending;
            o.orderDate = to;
            o.estimatedDelivery = to;
            o.clientNotes = "Notes added by client";
            orders.put(o.id, o);
            menuItems = new ArrayList<>();

            MenuItemRest menuItem1 = new MenuItemRest("Menu Item 1", "Food", "This is a description", 10.00, 10, 30, "/home/matteo/Immagini/icon", 0, Uri.EMPTY, new ArrayList<Integer>());
            menuItems.add(menuItem1);
            MenuItemRest menuItem2 = new MenuItemRest("Menu Item 2", "Food", "This is a description", 10.00, 10, 30, "/home/matteo/Immagini/icon", 1, Uri.EMPTY, new ArrayList<Integer>());
            menuItems.add(menuItem2);

            MenuItemRest menuItemRest = new MenuItemRest("Another Menu Item 1", "Drink", "This is a description", 10.30, 10, 21, "/home/matteo/Immagini/icon", 2, Uri.EMPTY, new ArrayList<Integer>());
            MenuItemRest menuItemRest2 = new MenuItemRest("Another Menu Item 2", "Drink", "This is a description", 15.30, 13, 10, "/home/matteo/Immagini/icon", 3, Uri.EMPTY, new ArrayList<Integer>());
            menuItems.add(menuItemRest);
            menuItems.add(menuItemRest2);

        }
    }

    public static void update(Order o) {
        Order old = orders.get(o.id);
        if (old != null) {
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

    public void addMenuItems(String name, String description, String category, String price, String availability, String time, String imgUri, Uri url, List<Integer> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, menuItems.size(), url, subItems);
        menuItems.add(item);
    }


    public static List<Order> getPendingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> pendings = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.pending)) pendings.add(o);
        }
        Collections.sort(pendings, myDateComparator);

        Log.d("MADAPP", "requested getPendingOrders() size=" + pendings.size());
        return pendings;
    }

    public static List<Order> getPreparingOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> preparing = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.preparing) || o.status.equals(OrderStatus.ready))
                preparing.add(o);
        }
        Collections.sort(preparing, myDateComparator);
        Log.d("MADAPP", "requested getPreparingOrders() ");
        return preparing;
    }

    public static List<Order> getCompletedOrders() {
        if (instance == null) {
            Log.d("MADAPP", "#### Database instance created");
            instance = new Database();
        }
        List<Order> completed = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status.equals(OrderStatus.completed) || o.status.equals(OrderStatus.canceled))
                completed.add(o);
        }
        Collections.sort(completed, myDateComparator);
        Log.d("MADAPP", "requested getCompletedOrders(), size is " + completed.size());
        return completed;
    }


    public void setMenuItems(Integer index, String name, String category, String description, String price, String availability, String time, String imgUri, Uri url, List<Integer> subItems) {
        MenuItemRest item = new MenuItemRest(name, category, description, Double.parseDouble(price), Integer.parseInt(availability), Integer.parseInt(time), imgUri, index, url, subItems);
       Log.d("INDICE: ", "L'indice è" + index);
        menuItems.set(index, item);
        Log.d("INDICE: ", "L'indice è" + index);

    }


    public List<MenuItemRest> getMenuItems() {
        return menuItems;
    }

    boolean removeMenuItem(MenuItemRest menuItemRest) {
        return menuItems.remove(menuItemRest);
    }

    public MenuItemRest getMenuItem(int id) {
        for (MenuItemRest menuItem : menuItems) {
            if (menuItem.id == id) {
                return menuItem;
            }
        }
        return null;
    }
}

