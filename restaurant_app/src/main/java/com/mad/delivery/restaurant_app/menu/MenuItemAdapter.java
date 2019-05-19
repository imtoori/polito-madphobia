package com.mad.delivery.restaurant_app.menu;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.restaurant_app.OnImageDownloaded;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.squareup.picasso.Picasso;

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
        ViewHolder viewHolder = new ViewHolder(view);


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
            if (!MenuFragment.offerIsEnabled()) {
                mListener.itemSelected(item);
                holder.star.setVisibility(View.GONE);
            }
        });
        if(MenuFragment.offerIsEnabled()) {
            holder.btnMinus.setVisibility(View.GONE);
        } else {
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.btnMinus.setOnClickListener(view1 -> {
                mListener.itemRemoved(holder.mItem);
            });
        }

        holder.clItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (MenuFragment.offerIsEnabled()) {
                    holder.selected = !holder.selected;
                    if (holder.selected) {
                        holder.star.setVisibility(View.VISIBLE);
                        mListener.addedToOffer(item);
                    } else {
                        holder.star.setVisibility(View.GONE);
                        mListener.removedFromOffer(item);
                    }
                    return true;
                }
                return false;
            }
        });

        if(holder.mItem.imageName != null) {
            if(!holder.mItem.imageName.equals("")) {
                RestaurantDatabase.getInstance().downloadImage(holder.mItem.restaurantId, "menu", holder.mItem.imageName, imageURI -> {
                    Picasso.get().load(imageURI.toString()).into(holder.menuItemImage);
                });
            }  else {
                holder.menuItemImage.setImageDrawable(view.getResources().getDrawable(R.drawable.restaurant_default, null));
            }
        } else {
            holder.menuItemImage.setImageDrawable(view.getResources().getDrawable(R.drawable.restaurant_default, null));
        }

    }

    @Override
    public int getItemCount() {
        if(items == null) return 0;
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public final ConstraintLayout clItem;
        public final ImageView menuItemImage;
        public final TextView name;
        public final TextView desc;
        public final ImageButton btnMinus;
        public final TextView availability;
        public final ImageView star;
        public MenuItemRest mItem;
        public boolean selected = false;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            menuItemImage = mView.findViewById(R.id.menuItemImage);
            name = mView.findViewById(R.id.tv_menuItemName);
            desc = mView.findViewById(R.id.tv_menuItemDescription);
            btnMinus = mView.findViewById(R.id.tv_removeMenuItem);
            availability = mView.findViewById(R.id.tv_menuItemAvailability);
            clItem = mView.findViewById(R.id.cl_dish_item);
            star = mView.findViewById(R.id.img_star);

        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
