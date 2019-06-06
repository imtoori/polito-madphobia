package com.mad.delivery.consumerApp;

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

import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.PreviewInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private List<PreviewInfo> favourites;
    private View view;
    private Drawable defaultImage;
    private OnFavouriteChange listener;

    public FavouritesAdapter(List<PreviewInfo> favourites, Drawable defaultImage, OnFavouriteChange listener) {
        this.defaultImage = defaultImage;
        this.favourites = favourites;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.favourite = favourites.get(position);
        holder.nameRestaurant.setText(holder.favourite.name);
        holder.descRestaurant.setText(holder.favourite.description);
        holder.rbRestaurant.setRating(holder.favourite.scoreValue.floatValue());
        holder.mView.setOnClickListener(v -> {
            listener.onOpen(holder.favourite);
        });
        holder.favouriteButton.setChecked(true);
        holder.favouriteButton.setOnClickListener(v -> {
            listener.onRemoved(holder.favourite.id);
        });
        if (holder.favourite.imageName != null) {
            ConsumerDatabase.getInstance().downloadImage(holder.favourite.id, "profile", holder.favourite.imageName, new OnImageDownloaded() {
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
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameRestaurant;
        public final TextView descRestaurant;
        public final ImageView imgRestaurant;
        public final RatingBar rbRestaurant;
        public PreviewInfo favourite;
        ToggleButton favouriteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameRestaurant = mView.findViewById(R.id.nameRestaurant);
            descRestaurant = mView.findViewById(R.id.descRestaurant);
            imgRestaurant = mView.findViewById(R.id.imgRestaurant);
            rbRestaurant = mView.findViewById(R.id.rateRestaurant);
            favouriteButton = mView.findViewById(R.id.button_favorite);
        }

        @Override
        public String toString() {
            return "ViewHolder";
        }
    }
}
