package com.mad.delivery.consumerApp.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.RestaurantCategory;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<RestaurantCategory> categories;
    private View view;
    private final CategoriesFragment.OnCategorySelected mListener;

    public CategoriesAdapter(List<RestaurantCategory> items, CategoriesFragment.OnCategorySelected listener) {
        categories = items;
        mListener = listener;
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
        holder.cvCategory.setBackground(holder.category.imageDrawable);
        List<String> categories = new ArrayList<>();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    categories.add(holder.category.name);
                    mListener.openCategory(categories, "", false, false);
                }
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
        public RestaurantCategory category;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameCategory = mView.findViewById(R.id.tv_name_category);
            cvCategory = mView.findViewById(R.id.cv_category);

        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + category.name;
        }
    }
}
