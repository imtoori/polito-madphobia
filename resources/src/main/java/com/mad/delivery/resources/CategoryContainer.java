package com.mad.delivery.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryContainer {
    public static CategoryContainer instance = new CategoryContainer();
    private static Map<String, Integer> categories;
    private CategoryContainer() {
        categories = new HashMap<>();
        categories.put("Pizza", R.drawable.pizza);
        categories.put("Hamburger", R.drawable.burger);
        categories.put("Grill", R.drawable.grill);
        categories.put("Japanese", R.drawable.jappo);
        categories.put("Thai", R.drawable.thai);
        categories.put("Chinese", R.drawable.chinese);
        categories.put("Veggie", R.drawable.veggie);
    }

    public static Integer get(String name) {
        Integer i = categories.get(name);
        if (i == null) {
            return R.drawable.pizza;
        } else {
            return i;
        }
    }

}
