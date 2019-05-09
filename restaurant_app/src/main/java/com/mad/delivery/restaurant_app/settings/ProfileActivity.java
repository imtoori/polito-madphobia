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
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.Database;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
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
    ImageView imgProfile;
    Restaurant mUser = new Restaurant();
    private ChipGroup chipGroup;
    private Set<String> categories;
    private Set<String> myCategories;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myToolBar = findViewById(R.id.mainActivityToolbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.profile_toolbar));
        name = findViewById(R.id.main_name);
        phoneNumber = findViewById(R.id.mainprofile_phone);
        emailAddress = findViewById(R.id.main_email);
        description = findViewById(R.id.main_description);
        opening = findViewById(R.id.openinghourslist);
        road = findViewById(R.id.main_road);
        imgProfile = findViewById(R.id.image_profile);
        chipGroup = findViewById(R.id.chip_group);
        categories = new HashSet<>();
        Log.d("MADAPP", "onCreate profile..");
        getProfileData();

        Database.getInstance().getCategories(mUser.getId(), set -> {
            myCategories = new HashSet<>(set);


            Database.getInstance().getCategories(innerSet -> {
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
                    if (myCategories.contains(n.toLowerCase())) {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                        chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                        chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    }
                    chipGroup.addView(chip);
                });
            });
        });


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_option:
                // start activity: EditProfileActivity
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("user", mUser);
                Log.d("MADAPP", mUser.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfileData() {
        Database.getInstance().getRestaurantProfile(new FireBaseCallBack<Restaurant>() {

            @Override
            public void onCallbackList(List<Restaurant> list) {

            }

            @Override
            public void onCallback(Restaurant user) {
                if (user != null) {
                    mUser = new Restaurant(user);
                    updateFields(mUser);
                    Log.d("MADAPP", "getProfileData(): " + mUser.toString());
                } else {
                    mUser = new Restaurant("", "", "", "", "", "", "", "", "", "", "", "");
                    updateFields(mUser);
                    Log.d("MADAPP", "(null..) getProfileData(): " + mUser.toString());
                }

            }
        });
    }

    private void updateFields(Restaurant u) {
        if (!u.name.equals(""))
            name.setText(u.name);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);
        if (!u.openingHours.equals("")) {
            opening.setText(u.openingHours);
        } else {
            opening.setText(getResources().getString(R.string.opening_hours_full));
        }

        if (!u.road.equals("")) {
            road.setText(u.road + ", " + u.houseNumber + ", " + u.postCode + " " + u.city + " (citofono: " + u.doorPhone + ")");
        }

        if (u.imageUri == null || u.imageUri == "") {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            imgProfile.setImageURI(Uri.parse(u.imageUri));
        }

        if (imgProfile.getDrawable() == null) {
            Database.getInstance().getImage(u.imageName, "/images/profile/", new FireBaseCallBack<Uri>() {
                @Override
                public void onCallback(Uri item) {
                    if (item != null) {
                        if (item == Uri.EMPTY || item.toString().equals("")) {
                            Log.d("MADAPP", "Setting user default image");

                            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                        } else {
                            Log.d("MADAPP", "Setting custom user image");
                            Log.d("DOWNLOAD2", item.toString());

                            // imgProfile.setImageURI(item);
                            Picasso.get().load(item.toString()).into(imgProfile);

                        }
                    }

                }

                @Override
                public void onCallbackList(List<Uri> list) {

                }
            });
        }
    }


    public void zoomImage(View view) {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, PhotoZoomActivity.class);
        if (mUser.imageUri == null)
            intent.putExtra("imageUri", "");
        else
            intent.putExtra("imageUri", mUser.imageUri.toString());
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