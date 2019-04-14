package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MenuItemRest mItem = menuItems.get(position);
        holder.price.setText(mItem.price.toString());
        holder.description.setText(mItem.description);
        holder.name.setText(mItem.name);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMenuItemActivity.class);
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
        public final TextView price;
        public Button button;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            //   titleView = mView.findViewById(R.id.menuItem).
            name = mView.findViewById(R.id.tv_menuItemsName);
            description = mView.findViewById(R.id.tv_menuItemsDescription);
            price = mView.findViewById(R.id.tv_menuItemsPrice);
            button = mView.findViewById(R.id.newMenuItem);


        }
    }
}
