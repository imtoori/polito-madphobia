package com.mad.delivery.consumerApp.settings;

import android.content.Intent;
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
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ProfileActivity extends AppCompatActivity {
    Toolbar myToolBar;
    Menu menu;
    TextView name;
    TextView phoneNumber;
    TextView emailAddress;
    TextView description;
    TextView road;
    ImageView imgProfile;
    User user;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Uri imageLink;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
            @Override
            public void onSuccess(User u) {
                user = u;
                Log.d("MADAPP","calling getProfileData with " + currentUser.getUid());
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
        road = findViewById(R.id.main_road);

        imgProfile = findViewById(R.id.image_profile);
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
                Bundle bundle = new Bundle();
                intent.putExtra("user", (Serializable) user);
                intent.putExtra("imageLink", imageLink);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProfileData(String id) {
        ConsumerDatabase.getInstance().getUserProfile(id, r -> {
            if (r != null && r.name != null) {
                user = new User(r);
            } else {
                user  = new User("","","",r.email,"","","","", "","",Uri.EMPTY,"");
                user.id = r.id;
            }
            Log.d("MADAPP", "user= "+user.toString());
            updateFields(user);
         });
    }

    private void updateFields(User u) {
        if(!u.name.equals("") || !u.lastName.equals(""))
            name.setText(u.name + " " + u.lastName);
        phoneNumber.setText(u.phoneNumber);
        Log.d("MADAPP", "called update fields: " + u.toString());
        emailAddress.setText(u.email);
        description.setText(u.description);
        if (!u.road.equals("")) {
            road.setText(u.road + ", " + u.houseNumber + ", " + u.postCode + " " + u.city + " (citofono: " + u.doorPhone + ")");
        }
        if (u.imageUri == null || u.imageUri.equals("")) {
            Log.d("MADAPP", "Setting user default image");
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            Log.d("MADAPP", "Setting custom user image");
            imgProfile.setImageURI(Uri.parse(u.imageUri));
        }

        if ( u.imageName != null && !u.imageName.equals("")) {
            ConsumerDatabase.getInstance().downloadImage(u.id, "profile", u.imageName, imageUri -> {
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
        if(user.imageUri==null)
        intent.putExtra("imageUri", "");
        else
            intent.putExtra("imageUri", user.imageUri.toString());

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
