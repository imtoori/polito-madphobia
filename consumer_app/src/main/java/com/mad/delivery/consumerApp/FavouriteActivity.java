package com.mad.delivery.consumerApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.RestaurantInfoActivity;
import com.mad.delivery.consumerApp.search.RestaurantsAdapter;
import com.mad.delivery.consumerApp.search.RestaurantsFragment;
import com.mad.delivery.resources.OnFirebaseData;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteActivity extends AppCompatActivity implements OnRestaurantSelectedF, OnUserLoggedCheck{
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private CardView emptyFolder;
    private RestaurantsAdapter restaurantsAdapter;
    private List<PreviewInfo> restaurant;
    private List<String> favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        myToolbar = (Toolbar) findViewById(R.id.favourite_toolbar);
        setTitle("Favourites");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        emptyFolder = findViewById(R.id.emptyfolder_cv);
        recyclerView = findViewById(R.id.restaurant_rv);
        restaurant = new ArrayList<>();

        favorite= new ArrayList<>();
        ConsumerDatabase.getInstance().getFavouriteRestaurants(new OnFirebaseData<List<String>>() {
            @Override
            public void onReceived(List<String> item) {
                favorite.addAll(item);

            }
        });
        restaurantsAdapter = new RestaurantsAdapter(restaurant, favorite,  this, getResources().getDrawable(R.drawable.restaurant_default, null), (OnUserLoggedCheck) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(restaurantsAdapter);
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

                }

                @Override
                public void onFailure() {
                    currentUser = null;
                }
            });
        }


        ConsumerDatabase.getInstance().getRestaurants(new HashSet<>(), "", false, false, null, null,  preview -> {
            if(preview != null) {
            if(favorite.contains(preview.id)) {
                restaurant.add(preview);
                restaurantsAdapter.notifyDataSetChanged();
            }
                if (restaurant.size() == 0) {
                    // show empty viewcard
                    emptyFolder.setVisibility(View.VISIBLE);
                } else {
                    emptyFolder.setVisibility(View.GONE);
                }
        }});

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
    public void openRestaurantF(PreviewInfo previewInfo) {
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra("restaurant", previewInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void success(boolean checked, String restaurantID) {
        if(currentUser != null) {
            if (checked) {
                Log.i("MADAPP", "favorite->enabled");
                ConsumerDatabase.getInstance().addFavouriteRestaurant(restaurantID);
            } else {

                Log.i("MADAPP", "favorite->disabled");
                ConsumerDatabase.getInstance().removeFavouriteRestaurant(restaurantID);
                favorite.removeIf(item -> item.equals(restaurantID));
                restaurant.removeIf(p -> p.id.equals(restaurantID));
                restaurantsAdapter.notifyDataSetChanged();


            }
        }
    }
}
