package com.mad.delivery.bikerApp;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DayOfWeekAdapter extends RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder> {

    private Map<Integer, List<TimeRange>> days;
    private View view;

    public DayOfWeekAdapter(Map<Integer, List<TimeRange>> items) {
        days = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daycardview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        List<TimeRange> timeRanges = days.get(index);
        holder.dayOfWeek.setText(getDayOfTheWeek(index));
        if(timeRanges.size() == 0) {
            holder.closed.setVisibility(View.VISIBLE);
        } else {
            holder.hoursReciclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            holder.hoursReciclerView.setAdapter(new HoursAdapter(timeRanges));
        }
    }



    @Override
    public int getItemCount() {
        return days.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements HoursAdapter.OnHoursChanges {
        public final View mView;
        public final TextView dayOfWeek;
        public final TextView closed;
        public final RecyclerView hoursReciclerView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            dayOfWeek = mView.findViewById(R.id.tv_day);
            hoursReciclerView = mView.findViewById(R.id.rl_opening_for_day);
            closed = mView.findViewById(R.id.hours_closed);
        }

        @Override
        public String toString() {
            return "View Holder";
        }

        @Override
        public void notifyTimeTableEmpty() {
                closed.setVisibility(View.VISIBLE);
        }
    }

    private String getDayOfTheWeek(int i) {
        switch(i) {
            case 0:
                return view.getResources().getString(R.string.monday);
            case 1:
                return view.getResources().getString(R.string.tuesday);
            case 2:
                return view.getResources().getString(R.string.wednesday);
            case 3:
                return view.getResources().getString(R.string.thursday);
            case 4:
                return view.getResources().getString(R.string.friday);
            case 5:
                return view.getResources().getString(R.string.saturday);
            case 6:
                return view.getResources().getString(R.string.sunday);
            default:
                return view.getResources().getString(R.string.monday);
        }
    }
}
