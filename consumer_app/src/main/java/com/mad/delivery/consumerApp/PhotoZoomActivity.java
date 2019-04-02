package com.mad.delivery.consumerApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
public class PhotoZoomActivity extends AppCompatActivity  {
    final String DEBUG_TAG = "MAD-APP";
    private GestureDetector gestureDetector;

    String className;
    Toolbar toolbar;
    ImageView imageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_zoom);
        toolbar = (Toolbar) findViewById(R.id.photoProfileToolbar);
        setTitle(getResources().getString(R.string.photo_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        imageProfile = findViewById(R.id.image_profile_zoom);

        Intent it = getIntent();

        Uri imageUri = Uri.parse(it.getStringExtra("imageUri"));
        className = it.getStringExtra("className");
        if(imageUri == null || imageUri.toString().equals("")) imageProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        else imageProfile.setImageURI(imageUri);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        imageProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getBack(className);
            }
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            getBack(className);
            return true;
        }
    }

    public void getBack(String className) {

        // Ordinary Intent for launching a new activity
        Intent intent;
        try {
            intent = new Intent(this, Class.forName(className));
        } catch(ClassNotFoundException e) {
            intent = new Intent(this, MainActivity.class);
        }
        // Get the transition name from the string
        String transitionName = getString(R.string.transition_zoom);

        // Define the view that the animation will start from
        View viewStart = findViewById(R.id.image_profile_zoom);

        ActivityOptions options =

                ActivityOptions.makeSceneTransitionAnimation(this,
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        startActivity(intent, options.toBundle());
        finishAfterTransition();
    }

}
