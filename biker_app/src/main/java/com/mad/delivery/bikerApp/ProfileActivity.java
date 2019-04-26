package com.mad.delivery.bikerApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    ImageView imgProfile;
    User mUser=new User();


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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("open", 1);
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
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        mUser = new User(sharedPref.getString("name", ""),
                sharedPref.getString("lastname", ""),
                sharedPref.getString("phoneNumber", ""),
                sharedPref.getString("emailAddress", ""),
                sharedPref.getString("description", ""),
                Uri.parse(sharedPref.getString("imageUri", Uri.EMPTY.toString()))
        );

        Log.d("MADAPP", "GET Profile data = "+ Uri.parse(sharedPref.getString("imageUri", Uri.EMPTY.toString())));
        if(mUser!=null) {
            Log.i("AAA", mUser.name + "-" + mUser.phoneNumber + "-" + mUser.email + "-" + mUser.description );
            updateFields(mUser);
        }
        else
            Log.i("AAA", "---------------------------------------------------------");
    }
    private void updateFields(User u) {
        if(!u.name.equals("") )
            name.setText(u.name+" "+u.lastname );
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);



        if (u.imageUri == Uri.EMPTY || u.imageUri.toString().equals("")) {
            Log.d("MADAPP", "Setting user default image");
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            Log.d("MADAPP", "Setting custom user image");
            imgProfile.setImageURI(u.imageUri);
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