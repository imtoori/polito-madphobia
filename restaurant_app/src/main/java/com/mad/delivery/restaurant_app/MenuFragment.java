package com.mad.delivery.restaurant_app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    static final String MENU_FRAGMENT_TAG = "menu_fragment";
    private View.OnClickListener onMenuItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

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
        List<MenuItem> menuItems = Database.getInstance().getMenuItems();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(menuItems, onMenuItemClick));

        return v;
    }

}
