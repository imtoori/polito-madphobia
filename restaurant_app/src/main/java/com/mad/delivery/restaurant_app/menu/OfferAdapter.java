package com.mad.delivery.restaurant_app.menu;

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

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private List<MenuItemRest> items;
    private View view;
    public OfferAdapter(List<MenuItemRest> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        MenuItemRest item = items.get(index);
        holder.mItem = item;
        holder.name.setText(item.name);
        holder.price.setText(String.valueOf(item.price));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public final TextView name;
        public final TextView price;
        public MenuItemRest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.item_name);
            price = mView.findViewById(R.id.item_price);

        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
