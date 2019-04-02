package com.mad.delivery.restaurantApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    TextView deliveryAddress;
    ImageView imgProfile;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolBar = findViewById(R.id.mainActivityToolbar);

        setSupportActionBar(myToolBar);
        setTitle(getResources().getString(R.string.profile_toolbar));
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailAddress = findViewById(R.id.email);
        description = findViewById(R.id.description);
        deliveryAddress = findViewById(R.id.deliveryAddress);
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
                         sharedPref.getString("phoneNumber", ""),
                         sharedPref.getString("emailAddress", ""),
                         sharedPref.getString("deliveryAddress", ""),
                         sharedPref.getString("description", ""),
                         Uri.parse(sharedPref.getString("imageUri", Uri.EMPTY.toString()))
                        );
        updateFields();
    }
    private void updateFields() {
        name.setText(mUser.name);
        phoneNumber.setText(mUser.phoneNumber);
        emailAddress.setText(mUser.email);
        description.setText(mUser.description);
        deliveryAddress.setText(mUser.deliveryAddress);
        if(mUser.imageUri.toString().equals(Uri.EMPTY.toString())) {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            imgProfile.setImageURI(mUser.imageUri);
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
