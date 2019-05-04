package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Order;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMenuItemOffertRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuItemOffertRecyclerViewAdapter.ViewHolder> {

    private final List<MenuItemRest> menuItems;
    private View view;
    private Context context;
    IOnMenuItemSelected onMenuItemSelected;

    public MyMenuItemOffertRecyclerViewAdapter(List<MenuItemRest> menuItems, Context context, IOnMenuItemSelected onMenuItemSelected) {
        this.menuItems = menuItems;
        this.context = context;
        this.onMenuItemSelected = onMenuItemSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MenuItemRest mItem = menuItems.get(position);
        holder.price.setText(mItem.price.toString());
        holder.description.setText(mItem.description);
        holder.availability.setText(mItem.availability.toString());
        holder.name.setText(mItem.name);


        //holder.image.setImageDrawable(getDrawable(R.drawable.user_default));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.selected = !holder.selected;

                if (holder.selected) {
                    holder.itemView.setBackgroundColor(Color.rgb(188,209,130));
                } else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }

                onMenuItemSelected.OnMenuItemSelected(mItem.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        // final TextView titleView;
        public final TextView name;
        public final TextView description;
        public final TextView price;
        public final TextView availability;
        public final ImageView image;
        /*    public final TextView availability;
            public final TextView category;*/
        public Button button;
        boolean selected = false;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            name = mView.findViewById(R.id.tv_menuItemsName);
            description = mView.findViewById(R.id.tv_menuItemsDescription);
            image = mView.findViewById(R.id.imageView2);
            price = mView.findViewById(R.id.tv_menuItemsPrice);
            button = mView.findViewById(R.id.newMenuItem);
            availability = mView.findViewById(R.id.tv_menuItemsAvailability);
        }
    }
}