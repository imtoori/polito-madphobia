package com.mad.delivery.restaurant_app;


import android.content.Intent;
import android.os.Bundle;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
    static final String MENU_FRAGMENT_TAG = "menu_fragment";

    public MenuFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.menu_list);

        // TODO remove items here when persistence is implemented
        List<MenuItemRest> menuItems = Database.getInstance().getMenuItems();
        List<String> categoryMenu = new ArrayList<String>();
        for(MenuItemRest i : menuItems){
            if(!categoryMenu.contains(i.category))
                categoryMenu.add(i.category);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyMenuItemCategoryRecyclerViewAdapter(categoryMenu,getContext()));


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View v) {

        FloatingActionButton button = v.findViewById(R.id.newMenuItem);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.newMenuItem:
                Intent intent = new Intent(getContext(), NewMenuItemActivity.class);
                startActivity(intent);
                break;


        }


    }

    void openMenuItem(MenuItem order) {
        Intent intent = new Intent(getContext(), NewMenuItemActivity.class);
        startActivity(intent);
    }
}

