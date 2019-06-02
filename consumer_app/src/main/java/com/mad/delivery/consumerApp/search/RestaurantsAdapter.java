package com.mad.delivery.consumerApp.search;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.firebaseCallback;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private List<PreviewInfo> restaurants;
    private View view;
    private final RestaurantsFragment.OnRestaurantSelected mListener;
    private Drawable defaultImage;

    public RestaurantsAdapter(List<PreviewInfo> items, RestaurantsFragment.OnRestaurantSelected listener, Drawable defaultImage) {
        restaurants = items;
        mListener = listener;
        this.defaultImage = defaultImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.restaurant = restaurants.get(position);
        ConsumerDatabase.getInstance().setResturantId(holder.restaurant.id);
        holder.nameRestaurant.setText(holder.restaurant.name);
        holder.descRestaurant.setText(holder.restaurant.description);
        holder.rbRestaurant.setRating(holder.restaurant.scoreValue.floatValue());
        if(holder.restaurant.imageName != null) {
            ConsumerDatabase.getInstance().downloadImage(holder.restaurant.id, "profile", holder.restaurant.imageName, new OnImageDownloaded() {
                @Override
                public void onReceived(Uri imageUri) {
                    Log.d("MADAPP", imageUri.toString());
                    if (imageUri == null || imageUri == Uri.EMPTY) {
                        holder.imgRestaurant.setImageDrawable(defaultImage);
                    } else {
                        Picasso.get().load(imageUri).into(holder.imgRestaurant);
                    }

                }
            });
        } else {
            holder.imgRestaurant.setImageDrawable(defaultImage);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.openRestaurant(holder.restaurant);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameRestaurant;
        public final TextView descRestaurant;
        public final ImageView imgRestaurant;
        public final RatingBar rbRestaurant;
        public PreviewInfo restaurant;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameRestaurant = mView.findViewById(R.id.nameRestaurant);
            descRestaurant = mView.findViewById(R.id.descRestaurant);
            imgRestaurant = mView.findViewById(R.id.imgRestaurant);
            rbRestaurant = mView.findViewById(R.id.rateRestaurant);

        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + restaurant.name;
        }
    }
}
