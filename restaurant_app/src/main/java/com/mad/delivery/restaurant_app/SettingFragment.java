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



public class SettingFragment extends Fragment {

    private RecyclerView rw;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OnSettingListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View view) {
        rw= (RecyclerView) view.findViewById(R.id.rl_setting);
        MySettingRecyclerViewAdapter adapter= new MySettingRecyclerViewAdapter(SettingItem.getData(), mListener);
        mAdapter=adapter;
        rw.setAdapter(mAdapter);
        layoutManager=new LinearLayoutManager(getActivity());
        rw.setLayoutManager(layoutManager);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingFragment.OnSettingListener) {
            mListener = (SettingFragment.OnSettingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSettingListener {
        void openProfile();
    }





}
