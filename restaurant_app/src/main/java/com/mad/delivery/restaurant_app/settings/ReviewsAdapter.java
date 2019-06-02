package com.mad.delivery.restaurant_app.settings;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.resources.TextAppearance;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.restaurant_app.R;

import org.joda.time.DateTime;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Feedback> items;
    private View view;
    public ReviewsAdapter(List<Feedback> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        Feedback item = items.get(index);
        holder.mItem = item;
        holder.date.setText(MyDateFormat.parse(new DateTime(item.date)));
        holder.foodVote.setText(Math.round(item.foodVote) + "/5.0" );
        holder.serviceVote.setText(Math.round(item.serviceRestaurantVote) + "/5.0" );
        holder.foodRating.setRating(item.foodVote.floatValue());
        holder.serviceRating.setRating(item.serviceRestaurantVote.floatValue());
        if(item.restaurantFeedback.equals("")) holder.comment.setTypeface(null, Typeface.ITALIC);
        else holder.comment.setText(item.restaurantFeedback);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public final TextView date;
        public final TextView foodVote, serviceVote;
        public final RatingBar foodRating, serviceRating;
        public final TextView comment;
        public Feedback mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date = mView.findViewById(R.id.item_date);
            foodVote = mView.findViewById(R.id.item_food_vote);
            serviceVote = mView.findViewById(R.id.item_service_vote);
            foodRating = mView.findViewById(R.id.item_food_rating);
            serviceRating = mView.findViewById(R.id.item_service_rating);
            comment = mView.findViewById(R.id.item_comment);

        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
