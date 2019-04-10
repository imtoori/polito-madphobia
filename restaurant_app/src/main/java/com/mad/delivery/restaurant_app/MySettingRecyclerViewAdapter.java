package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
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
    private LayoutInflater inflater;
    private Context context;


    public MySettingRecyclerViewAdapter(Context context, List<SettingItem> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.mDataList=data;


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= inflater.inflate(R.layout.setting_item_view, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        SettingItem current= mDataList.get(position);
        holder.icon.setImageResource(current.getImageId());
        holder.title.setText(current.getTitle());
        holder.itemView.setOnClickListener((v)->{
            Toast.makeText(context, holder.title.getText().toString(), Toast.LENGTH_SHORT).show();

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
