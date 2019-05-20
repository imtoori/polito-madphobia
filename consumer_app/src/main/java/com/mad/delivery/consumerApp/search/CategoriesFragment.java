package com.mad.delivery.consumerApp.search;


import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.GridRecyclerView;
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
        Log.i("MADAPP", "CategoriesFragment: onCREATED!");
        myRef = db.getReference("categories");
        categories = new ArrayList<>();
        Log.i("MADAPP", "categories->"+categories);
        categoriesAdapter = new CategoriesAdapter(categories, mListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categories, container, false);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);

        recyclerView = view.findViewById(R.id.category_rv);
        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutAnimation(animation);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //categories.clear();
        ConsumerDatabase.getInstance().getRestaurantCategories(categories,new ConsumerDatabase.onRestaurantCategoryReceived() {
            @Override
            public void childAdded(RestaurantCategory rc) {
                categories.add(rc);
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void childChanged(RestaurantCategory rc) {
                categories.stream().filter(c -> c.name.equals(rc.name)).map(c -> c = rc );
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void childMoved(RestaurantCategory rc) {
            }

            @Override
            public void childDeleted(RestaurantCategory rc) {
                categories.removeIf(c -> rc.name.equals(c.name));
                categoriesAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnCategorySelected {
        void openCategory(List<String> chosen, String address, boolean m, boolean d);
        boolean getMinOrderCostParam();
        boolean getDeliveryCostParam();

    }

}
