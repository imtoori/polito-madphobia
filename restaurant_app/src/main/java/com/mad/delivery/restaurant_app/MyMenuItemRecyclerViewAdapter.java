package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuItemRecyclerViewAdapter.ViewHolder> {

    private final List<MenuItemRest> menuItems;
    private View view;
     private Context context;
        public MyMenuItemRecyclerViewAdapter(List<MenuItemRest> menuItems,Context context) {
        this.menuItems = menuItems;
        this.context = context;
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
        holder.description.setText(mItem.description.toString());
        holder.availability.setText(mItem.availability.toString());
        holder.name.setText(mItem.name);
        if(mItem.imageUri!= Uri.EMPTY)
            holder.image.setImageURI(mItem.imageUri);

        //holder.image.setImageDrawable(getDrawable(R.drawable.user_default));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMenuItemActivity.class);
                intent.putExtra("id", mItem.id);
                context.startActivity(intent);

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
        public final TextView availability;
        public final TextView price;
        public final ImageView image;
    /*    public final TextView availability;
        public final TextView category;*/
        public Button button;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            //   titleView = mView.findViewById(R.id.menuItem).
            name = mView.findViewById(R.id.tv_menuItemsName);
            description = mView.findViewById(R.id.tv_menuItemsDescription);
            image = mView.findViewById(R.id.imageView2);
            price = mView.findViewById(R.id.tv_menuItemsPrice);
            button = mView.findViewById(R.id.newMenuItem);
            availability = mView.findViewById(R.id.tv_menuItemsAvailability);
        }
    }
}
