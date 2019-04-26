package com.mad.delivery.bikerApp.orders;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mad.delivery.bikerApp.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {

    private List<TimeRange> ranges;
    private View view;
    private HoursAdapter.OnHoursChanges mListener;
    interface OnHoursChanges {
        void notifyTimeTableEmpty();
    }
    public HoursAdapter(List<TimeRange> items) {
        ranges = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_for_opening_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int index) {
        TimeRange timeRange = ranges.get(index);
        holder.mItem = timeRange;
        holder.from.setText(timeRange.from.toString("hh:mm"));
        holder.to.setText(timeRange.to.toString("hh:mm"));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MADAPP", "CLICKED!!");
                ranges.remove(index);
                notifyDataSetChanged();
                if(ranges.size() == 0) {
                   // set the "closed" textview...
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ranges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView from;
        public final TextView to;
        public final ImageButton remove;
        public TimeRange mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            from = mView.findViewById(R.id.tv_from);
            to = mView.findViewById(R.id.tv_to);
            remove = mView.findViewById(R.id.btn_remove_hour);
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
