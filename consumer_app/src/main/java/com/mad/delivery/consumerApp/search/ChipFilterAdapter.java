package com.mad.delivery.consumerApp.search;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.RestaurantCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChipFilterAdapter extends RecyclerView.Adapter<ChipFilterAdapter.ViewHolder> {

    private List<RestaurantCategory> restaurantCategories;
    private View view;
    private OnFilterChanged mListener;

    public ChipFilterAdapter(List<RestaurantCategory> items, OnFilterChanged mListener){
        restaurantCategories = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_filter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.restaurantCategory = restaurantCategories.get(position);
        holder.chip.setText(holder.restaurantCategory.name);
        if(holder.restaurantCategory.selected) {
            holder.chip.setChecked(true);
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(holder.mView.getResources().getColor(R.color.colorPrimaryDark, null)));
            holder.chip.setTextColor(holder.mView.getResources().getColor(R.color.colorWhite, null));
            holder.chip.setChipIconTint(ColorStateList.valueOf(holder.mView.getResources().getColor(R.color.colorWhite, null)));
        }

        holder.chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mListener.changed(holder.chip, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Chip chip;
        public RestaurantCategory restaurantCategory;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            chip = view.findViewById(R.id.fil_chip);



        }

        @Override
        public String toString() {
            return "ViewHolder{";
        }
    }
}
