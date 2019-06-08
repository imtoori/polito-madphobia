package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.RestaurantInfoActivity;
import com.mad.delivery.consumerApp.search.RestaurantsAdapter;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteActivity extends AppCompatActivity implements OnFavouriteChange {
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private CardView emptyFolder;
    private FavouritesAdapter favouritesAdapter;
    private List<PreviewInfo> favourites;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        mAuth = FirebaseAuth.getInstance();
        myToolbar = findViewById(R.id.favourite_toolbar);
        setTitle("My Favourites");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.pg_bar);
        emptyFolder = findViewById(R.id.emptyfolder_cv);
        recyclerView = findViewById(R.id.restaurant_rv);
        favourites = new ArrayList<>();

        favouritesAdapter = new FavouritesAdapter(favourites, getResources().getDrawable(R.drawable.restaurant_default, null), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(favouritesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
                @Override
                public void onSuccess(User u) {
                    ConsumerDatabase.getInstance().getFavouriteRestaurants(u.id, preview -> {
                        if (preview != null) {
                            boolean found = false;
                            for(PreviewInfo p : favourites) {
                                if(p.id.equals(preview.id)) {
                                    found = true; break;
                                }
                            }

                            if(!found) {
                                favourites.add(preview);
                                favouritesAdapter.notifyDataSetChanged();
                                if (favourites.size() == 0) {
                                    emptyFolder.setVisibility(View.VISIBLE);
                                } else {
                                    emptyFolder.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        if (favourites.size() == 0) {
                            emptyFolder.setVisibility(View.VISIBLE);
                        } else {
                            emptyFolder.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onFailure() {
                    currentUser = null;
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("open", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onOpen(PreviewInfo previewInfo) {
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra("restaurant", previewInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRemoved(String restaurantID) {
        if (currentUser != null) {
            ConsumerDatabase.getInstance().removeFavouriteRestaurant(restaurantID, currentUser.getUid(), value -> {
                Log.d("MADAPP", "before is " + favourites.size());
                favourites.removeIf(item -> item.id.equals(restaurantID));
                favouritesAdapter.notifyDataSetChanged();
                Log.d("MADAPP", "now is " + favourites.size());
                if (favourites.size() == 0) {
                    emptyFolder.setVisibility(View.VISIBLE);
                }
            });

        }
    }

}

