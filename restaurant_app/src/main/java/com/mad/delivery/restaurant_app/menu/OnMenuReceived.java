package com.mad.delivery.restaurant_app.menu;

import com.mad.delivery.resources.MenuItemRest;

import java.util.List;
import java.util.Map;

public interface OnMenuReceived {
    void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories);
    void itemRemoved(MenuItemRest item);

}
