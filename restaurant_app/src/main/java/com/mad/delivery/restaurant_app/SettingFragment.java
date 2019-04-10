package com.mad.delivery.restaurant_app;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import static com.mad.delivery.restaurant_app.R.id.orders_pager;

public class SettingFragment extends Fragment {
    public static final String ORDER_FRAGMENT_TAG = "order_fragment";

    private RecyclerView rw;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View view) {
        rw= (RecyclerView) view.findViewById(R.id.rl_setting);
        MySettingRecyclerViewAdapter adapter= new MySettingRecyclerViewAdapter(getActivity(), SettingItem.getData());
        mAdapter=adapter;
        rw.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(getActivity());
        rw.setLayoutManager(layoutManager);
    }





}
