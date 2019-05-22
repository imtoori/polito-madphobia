package com.mad.delivery.restaurant_app.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

public class GeneralSettingActivity extends AppCompatActivity {
    private Switch switchVisible;
    private TextView tvCompleted;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Restaurant restaurant;
    private Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_setting);
        mAuth = FirebaseAuth.getInstance();

        myToolbar =  findViewById(R.id.myToolbar);
        setTitle(getResources().getString(R.string.setting_general));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        switchVisible = findViewById(R.id.switch_visible);
        tvCompleted = findViewById(R.id.tv_profile_status_content);
        switchVisible.setChecked(false);
        switchVisible.setEnabled(false);

        switchVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RestaurantDatabase.getInstance().updateRestaurantVisibility(restaurant.previewInfo.id, b, item -> {
                    // do nothing
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
                @Override
                public void onSuccess(Restaurant user) {
                    Log.d("MADAPP", "User "+ user.previewInfo.id + " have logged in.");
                    restaurant = user;
                    Log.d("MADAPP", "checking if profile is complete returned:" + restaurant.isProfileComplete());
                    if(restaurant.isProfileComplete()) {
                        tvCompleted.setText("complete");
                        tvCompleted.setTextColor(getColor(R.color.colorGreen));
                        switchVisible.setEnabled(true);
                        if(restaurant.visible == null) switchVisible.setChecked(false);
                        else switchVisible.setChecked(restaurant.visible);
                    } else {
                        tvCompleted.setText("uncomplete");
                        tvCompleted.setTextColor(getColor(R.color.colorRed));
                    }
                }

                @Override
                public void onFailure() {
                    Log.d("MADAPP", "User "+ currentUser.getUid() + " can't log in.");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
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
}
