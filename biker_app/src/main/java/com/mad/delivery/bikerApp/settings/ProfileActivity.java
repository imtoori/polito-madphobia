package com.mad.delivery.bikerApp.settings;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.bikerApp.Database;
import com.mad.delivery.bikerApp.FirebaseCallbackItem;
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    ImageView imgProfile;
    Biker mUser=new Biker();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.profile_toolbar));
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.main_name);
        phoneNumber = findViewById(R.id.mainprofile_phone);
        emailAddress = findViewById(R.id.main_email);
        description = findViewById(R.id.main_description);
        imgProfile = findViewById(R.id.image_profile);
        getProfileData();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfileData() {
        Database.getInstance().getBikerProfile(new FirebaseCallbackItem<Biker>(){
            @Override
            public void onCallback(Biker user) {
                if(user.id!=null){
                    mUser = new Biker(user);
                    updateFields(mUser);
                }
                else{
                    mUser  = new Biker("","","","","", Uri.EMPTY);
                    updateFields(mUser);
                }

            }
        });
    }
    private void updateFields(Biker u) {
        if (!u.name.equals(""))
            name.setText(u.name + " " + u.lastname);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);


        imgProfile.setImageURI(Uri.parse(u.imageUri));

        if (imgProfile.getDrawable() == null) {
            Database.getInstance().getImage(u.imageName, "/images/profile/", new FirebaseCallbackItem<Uri>() {
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
        if(mUser.imageUri==null)
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