package com.mad.delivery.restaurant_app.menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements OnMenuChanged {
    private RecyclerView recyclerView;
    private MyMenuItemCategoryRecyclerViewAdapter adapter;
    private Map<String, List<MenuItemRest>> menu;
    private List<String> categories;
    private CardView cvEmpty;
    private Restaurant restaurant;
    private Button btnNewDish, btnNewOffer;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        recyclerView = v.findViewById(R.id.rv_menu);
        btnNewDish = v.findViewById(R.id.btn_new_dish);
        btnNewOffer = v.findViewById(R.id.btn_new_offer);
        menu = new HashMap<>();
        categories = new ArrayList<>();
        btnNewDish.setOnClickListener(view -> {
            openNewDishActivity(null);
        });

        cvEmpty = v.findViewById(R.id.empty_cardview);
        adapter = new MyMenuItemCategoryRecyclerViewAdapter(menu, categories, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        RestaurantDatabase.getInstance().getMenu(currentUser.getUid(), new OnMenuReceived() {
            @Override
            public void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories) {
                MenuFragment.this.menu.putAll(menu);
                MenuFragment.this.categories.addAll(categories);
                adapter.notifyDataSetChanged();
                if(menu.size() != 0) {
                    cvEmpty.setVisibility(View.GONE);
                } else {
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void itemRemoved(MenuItemRest item) {
                // nothing happens
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_menu, menu);
    }


    @Override
    public void itemRemoved(MenuItemRest item) {
        RestaurantDatabase.getInstance().removeMenuItem(item.restaurantId, item, new OnMenuReceived() {
            @Override
            public void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories) {
                // nothing
            }
            @Override
            public void itemRemoved(MenuItemRest item) {
                List<MenuItemRest> menuList = menu.get(item.category);
                if (menuList != null) {
                    menuList.removeIf(i -> i.name.equals(item.name));
                    if (menuList.size() == 0) {
                        menu.remove(item.category);
                        categories.removeIf(i -> item.category.equals(i));
                    }
                    adapter.notifyDataSetChanged();
                }
                if (menu.size() == 0) {
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void itemSelected(MenuItemRest item) {
        openNewDishActivity(item);
    }


    public void openNewDishActivity(MenuItemRest item) {
        Intent intent = new Intent(getContext(), NewMenuItemActivity.class);
        ArrayList<String> menuList = new ArrayList<>(menu.keySet());
        intent.putStringArrayListExtra("categories", menuList);
        if(item != null) {
            intent.putExtra("item", (Parcelable) item);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void createNewOffer() {

    }
}
