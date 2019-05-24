package com.mad.delivery.restaurant_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.DistanceBiker;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.restaurant_app.menu.MenuFragment;
import com.mad.delivery.restaurant_app.menu.OnMenuChanged;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BikerAdapter extends RecyclerView.Adapter<BikerAdapter.ViewHolder> {
    private List<DistanceBiker> items;
    private View view;
    private OnBikerChanged mListener;
    public BikerAdapter(List<DistanceBiker> items, OnBikerChanged mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_biker_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        DistanceBiker item = items.get(index);
        holder.mItem = item;
        holder.name.setText(item.biker.name +  " " + item.biker.lastname);
        holder.distance.setText(item.distance + " km");
        holder.selectedCv.setOnClickListener( v-> {
            mListener.selected(holder.mItem.biker);
        });
    }

    @Override
    public int getItemCount() {
        if(items == null) return 0;
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public DistanceBiker mItem;
        public TextView name, distance;
        public CardView selectedCv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.biker_name);
            distance = mView.findViewById(R.id.biker_distance);
            selectedCv = mView.findViewById(R.id.cv_selected);
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
