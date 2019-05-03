package com.mad.delivery.consumerApp.search;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.resources.GridRecyclerView;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.RestaurantCategory;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private GridRecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private OnCategorySelected mListener;
    private List<RestaurantCategory> categories;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference myRef;
    private ImageButton search;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Log.d("MADAPP", "SearchFragment: onCREATED!");
        myRef = db.getReference("categories");
        mFirebaseStorage = FirebaseStorage.getInstance();
        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categories, mListener);
        ConsumerDatabase.getInstance().getRestaurantCategories(new ConsumerDatabase.onRestaurantCategoryReceived() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
        search = view.findViewById(R.id.search_imgbtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openCategory(null, "");
            }
        });
        recyclerView = view.findViewById(R.id.category_rv);
        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutAnimation(animation);
        return view;
    }

    public interface OnCategorySelected {
        void openCategory(RestaurantCategory category, String address);
    }
}
