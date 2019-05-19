package com.mad.delivery.restaurant_app.settings;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.OnImageDownloaded;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    TextView opening;
    TextView road;
    TextView deliveryCost, minOrderCost;
    ImageView imgProfile;
    Restaurant restaurant;
    private ChipGroup chipGroup;
    private Set<String> categories;
    private Set<String> myCategories;
    private FirebaseAuth mAuth;
    private Uri imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myToolBar = findViewById(R.id.profile_toolbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getResources().getString(R.string.profile_toolbar));
        name = findViewById(R.id.main_name);
        phoneNumber = findViewById(R.id.mainprofile_phone);
        emailAddress = findViewById(R.id.main_email);
        description = findViewById(R.id.main_description);
        opening = findViewById(R.id.openinghourslist);
        road = findViewById(R.id.main_road);
        imgProfile = findViewById(R.id.image_profile);
        chipGroup = findViewById(R.id.chip_group);
        deliveryCost = findViewById(R.id.tv_delivery_fee);
        minOrderCost = findViewById(R.id.tv_min_order);
        categories = new HashSet<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant user) {
                restaurant = user;
                getProfileData(currentUser.getUid());
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("open", 2);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_option:
                // start activity: EditProfileActivity
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("user", (Serializable) restaurant);
                intent.putExtra("imageLink", imageLink);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfileData(String id) {
        RestaurantDatabase.getInstance().getRestaurantProfile(id, r -> {
            if (r != null && r.previewInfo != null && r.previewInfo.name != null) {
                restaurant = new Restaurant(r);
            } else {
                restaurant = new Restaurant(id, "", r.email, "", "", "", "", "", "", "", "", "", "");
            }
            RestaurantDatabase.getInstance().getCategories(innerSet -> {
                innerSet.stream().forEach(n -> {
                    Chip chip = new Chip(this);
                    chip.setText(n);
                    chip.setChipBackgroundColorResource(R.color.colorWhite);
                    chip.setChipStrokeWidth((float) 0.1);
                    chip.setChipStrokeColorResource(R.color.colorPrimary);
                    chip.setCheckable(false);
                    chip.setEnabled(false);
                    chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    chip.setRippleColorResource(R.color.colorPrimaryDark);
                    chip.setCheckedIconVisible(false);
                    chip.setChipIconTintResource(R.color.colorPrimary);
                    if (restaurant.categories != null) {
                        if (restaurant.categories.containsKey(n.toLowerCase())) {
                            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                            chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                            chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                        }
                    }
                    chipGroup.addView(chip);
                });
            });
            updateFields(restaurant);

        });

    }

    private void updateFields(Restaurant u) {
        if (!u.previewInfo.name.equals(""))
            name.setText(u.previewInfo.name);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.previewInfo.description);
        deliveryCost.setText(String.valueOf(u.previewInfo.deliveryCost));
        minOrderCost.setText(String.valueOf(u.previewInfo.minOrderCost));
        if (!u.openingHours.equals("")) {
            opening.setText(u.openingHours);
        } else {
            opening.setText(getResources().getString(R.string.opening_hours_full));
        }

        if (!u.road.equals("")) {
            road.setText(u.road + ", " + u.houseNumber + ", " + u.postCode + " " + u.city + " (citofono: " + u.doorPhone + ")");
        }

        if (u.previewInfo != null && u.previewInfo.imageName != null && !u.previewInfo.imageName.equals("")) {
            RestaurantDatabase.getInstance().downloadImage(u.previewInfo.id, "profile", u.previewInfo.imageName, imageUri -> {
                if (imageUri == null || imageUri.toString().equals("") || imageUri.equals(Uri.EMPTY)) {
                    imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                } else {
                    Picasso.get().load(imageUri.toString()).into(imgProfile);
                    imageLink = imageUri;
                }
            });
        } else {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        }
    }


    public void zoomImage(View view) {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, PhotoZoomActivity.class);
        if (restaurant.previewInfo.imageName == null)
            intent.putExtra("imageUri", "");
        else
            intent.putExtra("imageUri", restaurant.previewInfo.imageName.toString());
        intent.putExtra("className", this.getClass().getName());
        // Get the transition name from the string
        String transitionName = getString(R.string.transition_zoom);

        // Define the view that the animation will start from
        View viewStart = findViewById(R.id.image_profile);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}