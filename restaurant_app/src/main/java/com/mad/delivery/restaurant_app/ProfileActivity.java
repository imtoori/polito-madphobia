package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    TextView opening;
    TextView road;
    ImageView imgProfile;
    Restaurant mUser=new Restaurant();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myToolBar = findViewById(R.id.mainActivityToolbar);

        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.profile_toolbar));
        name = findViewById(R.id.main_name);
        phoneNumber = findViewById(R.id.mainprofile_phone);
        emailAddress = findViewById(R.id.main_email);
        description = findViewById(R.id.main_description);
        opening = findViewById(R.id.openinghourslist);
        road = findViewById(R.id.main_road);
        imgProfile = findViewById(R.id.image_profile);
        getProfileData();
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
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfileData() {

        Database.getInstance().getRestaurantProfile(new FirebaseCallbackUser(){
            @Override
            public void onCallbak(Restaurant user) {
                if(user!=null){
                    mUser = new Restaurant(user);
                    updateFields(mUser);
                }
                else{
                    mUser  = new Restaurant("","","","","","","","", "","","","");
                       updateFields(mUser);
                    }

            }
        });
    }

    private void updateFields(Restaurant u) {
        if(!u.name.equals("") )
            name.setText(u.name );
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);
        if(!u.openingHours.equals("")) {
            opening.setText(u.openingHours);
        } else {
            opening.setText(getResources().getString(R.string.opening_hours_full));
        }

        if(!u.road.equals("")) {
            road.setText(u.road + ", " + u.houseNumber + ", " + u.postCode + " " + u.city + " (citofono: " + u.doorPhone + ")");
        }
        //Picasso.get().load(u.imageUri.toString()).into(imgProfile);
       // Log.d("----",Uri.parse(u.imageUri).getPath());
        // imageProfileUri = Uri.parse( mUser.imageUri);

        imgProfile.setImageURI(Uri.parse(u.imageUri));

        if(imgProfile.getDrawable() == null) {
            Database.getInstance().getImage(u.imageName,"/images/profile/", new Callback() {
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
            });
        }
    }


    public void zoomImage(View view) {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, PhotoZoomActivity.class);
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