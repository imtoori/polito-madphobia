package com.mad.delivery.restaurant_app.menu;

import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MenuOffer;

import java.util.List;
import java.util.Map;

public interface OnMenuChanged {
    void itemRemoved(MenuItemRest item);
    void itemSelected(MenuItemRest item);
    void addedToOffer(MenuItemRest item);
    void removedFromOffer(MenuItemRest item);
    void offerRemoved(MenuOffer item);
}
