package com.mad.delivery.restaurant_app.menu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.MenuCategory;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Order;
import com.mad.delivery.restaurant_app.orders.PendingOrdersFragment;
import com.mad.delivery.restaurant_app.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMenuItemCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuItemCategoryRecyclerViewAdapter.ViewHolder>{
    Map<String, List<MenuItemRest>> menu;
    List<String> categories;
    private View view;
    private Context context;
    private OnMenuChanged onMenuChanged;
    public MyMenuItemCategoryRecyclerViewAdapter(Map<String, List<MenuItemRest>> menu, List<String> categories, OnMenuChanged onMenuChanged) {
        this.menu = menu;
        this.categories = categories;
        this.onMenuChanged = onMenuChanged;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String categoryName = categories.get(position);
        holder.menuItemsList = menu.get(categoryName);
        holder.name.setText(categoryName);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        holder.recyclerView.setAdapter(new MenuItemAdapter(holder.menuItemsList, onMenuChanged));
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final RecyclerView recyclerView;
        public List<MenuItemRest> menuItemsList;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.tv_menuItemsCategoryName);
            recyclerView = mView.findViewById(R.id.rv_menuItems);
        }
    }
}