package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Order;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMenuItemCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuItemCategoryRecyclerViewAdapter.ViewHolder> {

    List<String> categories;
    private View view;
    private Context context;
    public MyMenuItemCategoryRecyclerViewAdapter(List<String> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String mItem = categories.get(position);

        holder.name.setText(mItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("category", mItem);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.tv_menuItemsCategoryName);
        }
    }
}