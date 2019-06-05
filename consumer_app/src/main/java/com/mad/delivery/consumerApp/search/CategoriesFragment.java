package com.mad.delivery.consumerApp.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.GridRecyclerView;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.RestaurantCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {
    private GridRecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private OnCategorySelected mListener;
    private List<RestaurantCategory> categories;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase db;
    private ProgressBar pgBar;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnCategorySelected) {
            mListener = (OnCategorySelected) getParentFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategorySelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("categories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categories, mListener, getContext());
        pgBar = view.findViewById(R.id.pg_bar);
        recyclerView = view.findViewById(R.id.category_rv);
        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutAnimation(animation);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConsumerDatabase.getInstance().getRestaurantCategories(categories, new ConsumerDatabase.onRestaurantCategoryReceived() {
            @Override
            public void isEmpty() {
                pgBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void childAdded(RestaurantCategory rc) {
                if (rc != null) {
                    categories.add(rc);
                    categoriesAdapter.notifyDataSetChanged();
                    //mListener.closeFilters();
                    pgBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.d("MADAPP", "No categories..");
                    pgBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void childChanged(RestaurantCategory rc) {
                if (rc != null) {
                    for(RestaurantCategory rItem : categories) {
                        if(rItem.name.equals(rc.name)) {
                            rItem = rc;
                            categoriesAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    //mListener.closeFilters();

                } else {
                    Log.d("MADAPP", "No categories..");
                }
                pgBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void childMoved(RestaurantCategory rc) {
                if (rc != null) {
                    for(RestaurantCategory rItem : categories) {
                        if(rItem.name.equals(rc.name)) {
                            rItem = rc;
                            categoriesAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    //mListener.closeFilters();

                } else {
                    Log.d("MADAPP", "No categories..");
                }
                pgBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void childDeleted(RestaurantCategory rc) {
                if (rc != null) {
                    categories.removeIf(r -> r.name.equals(rc.name));
                    categoriesAdapter.notifyDataSetChanged();
                    //mListener.closeFilters();
                } else {
                    Log.d("MADAPP", "No categories..");
                }
                pgBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

    }


    public interface OnCategorySelected {
        void openCategory(List<String> chosen, String address, boolean m, boolean d);
        boolean getMinOrderCostParam();
        boolean getDeliveryCostParam();
        void closeFilters();

    }

}
