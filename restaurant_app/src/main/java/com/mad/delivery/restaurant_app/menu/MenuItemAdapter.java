package com.mad.delivery.restaurant_app.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.restaurant_app.R;

import java.util.HashMap;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItemRest> items;
    private View view;
    private final OnMenuChanged mListener;
    public MenuItemAdapter(List<MenuItemRest> items, OnMenuChanged mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        MenuItemRest item = items.get(index);
        holder.mItem = item;
        holder.name.setText(item.name);
        holder.desc.setText(item.description);
        holder.menuItemImage.setImageDrawable(view.getResources().getDrawable(R.drawable.burger, null));
        holder.availability.setText(String.valueOf(item.availability));
        holder.clItem.setOnClickListener(view -> {
            mListener.itemSelected(item);
        });
    }

    @Override
    public int getItemCount() {
        if(items == null) return 0;
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ConstraintLayout clItem;
        public final ImageView menuItemImage;
        public final TextView name;
        public final TextView desc;
        public final ImageButton btnMinus;
        public final TextView availability;
        public MenuItemRest mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            menuItemImage = mView.findViewById(R.id.menuItemImage);
            name = mView.findViewById(R.id.tv_menuItemName);
            desc = mView.findViewById(R.id.tv_menuItemDescription);
            btnMinus = mView.findViewById(R.id.tv_removeMenuItem);
            availability = mView.findViewById(R.id.tv_menuItemAvailability);
            clItem = mView.findViewById(R.id.cl_dish_item);

            btnMinus.setOnClickListener(view1 -> {
                mListener.itemRemoved(mItem);
            });
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
