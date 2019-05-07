package com.mad.delivery.restaurant_app.menu;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.restaurant_app.Database;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
    public MenuFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = v.findViewById(R.id.menu_list);

        MyMenuItemCategoryRecyclerViewAdapter adapter = new MyMenuItemCategoryRecyclerViewAdapter(new ArrayList<>(), getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        // TODO remove items here when persistence is implemented
        Database.getInstance().getMenuItems(new OnDataFetched<List<MenuItemRest>, String>() {
            @Override
            public void onDataFetched(List<MenuItemRest> menuItems) {
                List<String> categoryMenu = new ArrayList<>();
                for (MenuItemRest i : menuItems) {
                    if (!categoryMenu.contains(i.category))
                        categoryMenu.add(i.category);
                }
                adapter.categories = categoryMenu;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error reading data: " + error, Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_menu,menu);
    }


}
