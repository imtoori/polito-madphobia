package com.mad.delivery.consumerApp.search;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.mad.delivery.resources.GridRecyclerView;
import com.mad.delivery.consumerApp.FoodCategory;
import com.mad.delivery.consumerApp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private GridRecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private OnCategorySelected mListener;
    private List<FoodCategory> categories;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategorySelected) {
            mListener = (OnCategorySelected) context;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);


        recyclerView = view.findViewById(R.id.category_rv);
        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categories, mListener);

        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FoodCategory pizza = new FoodCategory("Pizza", "Best pizza ever", R.drawable.pizza);
        FoodCategory burger = new FoodCategory("Burger", "", R.drawable.burger);
        FoodCategory grill = new FoodCategory("Grill", "Best grill ever", R.drawable.grill);
        FoodCategory jappo = new FoodCategory("Japanese", "Best Japanese ever", R.drawable.jappo);
        FoodCategory chinese = new FoodCategory("Chinese", "Best Chinese ever", R.drawable.chinese);
        FoodCategory veggie = new FoodCategory("Veggie", "Best Veggie ever", R.drawable.veggie);
        FoodCategory thai = new FoodCategory("Thai", "Best Thai ever", R.drawable.thai);
        categories.add(pizza);
        categories.add(burger);
        categories.add(grill);
        categories.add(jappo);
        categories.add(chinese);
        categories.add(veggie);
        categories.add(thai);
        categoriesAdapter.notifyDataSetChanged();
    }

    public interface OnCategorySelected {
        void openCategory();
    }

}
