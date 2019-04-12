package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MySettingRecyclerViewAdapter extends RecyclerView.Adapter<MySettingRecyclerViewAdapter.ViewHolder> {

    private List<SettingItem> mDataList = Collections.emptyList();
    private View view;
    private final SettingFragment.OnSettingListener mListener;

    public MySettingRecyclerViewAdapter(List<SettingItem> data, SettingFragment.OnSettingListener listener){
        this.mDataList=data;
        mListener=listener;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item_view, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        SettingItem current= mDataList.get(position);
        holder.icon.setImageResource(current.getImageId());
        holder.title.setText(current.getTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    switch (holder.title.getText().toString()){
                        case "Profile":
                            mListener.openProfile();
                            break;
                        case "Password":
                            mListener.openPssw();
                            break;
                        case "Language":
                            mListener.openLanguage();
                            break;
                        default:

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final ImageView icon;
        public SettingItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = mView.findViewById(R.id.title_s);
            icon = mView.findViewById(R.id.imgIcon_s);

        }

    }
}
