package com.mad.delivery.consumerApp.search;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.MenuCategory;

import java.util.List;

public class MenuCategoriesAdapter extends RecyclerView.Adapter<MenuCategoriesAdapter.ViewHolder> {

    private List<MenuCategory> categories;
    private View view;

    public MenuCategoriesAdapter(List<MenuCategory> categories) {
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_expandable_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.category = categories.get(position);
        holder.nameCategory.setText(holder.category.name);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        holder.recyclerView.setAdapter(new MenuItemAdapter(holder.category.items));

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameCategory;
        public final RecyclerView recyclerView;
        public MenuCategory category;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameCategory = mView.findViewById(R.id.tv_menu_category);
            recyclerView = mView.findViewById(R.id.menuitemsrest_rv);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + category.name;
        }
    }
}