package com.mad.delivery.consumerApp.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.RestaurantCategory;
import com.squareup.picasso.Picasso;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<RestaurantCategory> categories;
    private View view;
    private final CategoriesFragment.OnCategorySelected mListener;
    DisplayMetrics metrics;
    int width;
    int height;

    public CategoriesAdapter(List<RestaurantCategory> items, CategoriesFragment.OnCategorySelected listener, Context context) {
        categories = items;
        mListener = listener;
        this.metrics = context.getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.category = categories.get(position);
        holder.nameCategory.setText(holder.category.name);
        //Picasso.get().load(holder.category.imageURL).resize(width/2, 500).into(holder.imageCategory);
        List<String> categories = new ArrayList<>();
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                categories.add(holder.category.name.toLowerCase());
                mListener.openCategory(categories, "", false, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameCategory;
        public final CardView cvCategory;
        public final ImageView imageCategory;
        public final boolean setted;
        public RestaurantCategory category;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameCategory = mView.findViewById(R.id.tv_name_category);
            cvCategory = mView.findViewById(R.id.cv_category);
            imageCategory = mView.findViewById(R.id.tv_image_category);
            setted = false;

        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + category.name;
        }
    }
}
