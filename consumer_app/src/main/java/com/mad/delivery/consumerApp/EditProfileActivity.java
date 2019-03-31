package com.mad.delivery.consumerApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.mad.delivery.clientApp.BuildConfig;
import com.mad.delivery.clientApp.R;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.

    SharedPreferences sharedPref;
    Menu menu;
    User mUser, temp;
    Button btnCamera;
    EditText name;
    EditText phoneNumber;
    EditText emailAddress;
    EditText description;
    EditText deliveryAddress;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    final int SHOW_IMAGE_FULL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MADAPP", "onCreate()");
        if (savedInstanceState != null) {
            Log.d("MADAPP", "SavedInstanceState contains data");
            mUser.name = savedInstanceState.getString("name");
            mUser.phoneNumber = savedInstanceState.getString("phoneNumber");
            mUser.email = savedInstanceState.getString("emailAddress");
            mUser.description = savedInstanceState.getString("description");
            mUser.deliveryAddress = savedInstanceState.getString("deliveryAddress");
            mUser.imageUri = savedInstanceState.getString("imageUri");
            updateFields(mUser);
        } else Log.d("MADAPP", "SavedInstanceState is null");
        setContentView(R.layout.activity_edit_profile);

        myToolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setTitle(R.string.editprofile_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = findViewById(R.id.editprofile_name);
        phoneNumber = findViewById(R.id.editprofile_phone);
        emailAddress = findViewById(R.id.editprofile_email);
        description = findViewById(R.id.editprofile_description);
        deliveryAddress = findViewById(R.id.editprofile_deliveryAddress);
        imgProfile = findViewById(R.id.editprofile_imgprofile);
        btnCamera = findViewById(R.id.editprofile_btncamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(EditProfileActivity.this);
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImage();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();

    }

    @Override
    protected void onPause() {
        super.onPause();
        setProfileData();
        Log.d("MADAPP", "Profile data are saved");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileData();
        Log.d("MADAPP", "Profile data are restored");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.editprofile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // invoked when the activity may be temporarily destroyed, save the instance state here
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("phoneNumber", phoneNumber.getText().toString());
        outState.putString("emailAddress", emailAddress.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("deliveryAddress", description.getText().toString());
        if (imageProfileUri != null)
            outState.putString("imageUri", imageProfileUri.toString());
        else
            outState.putString("imageUri", "");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //The system calls onRestoreInstanceState(), after the onStart() method,
        //only if there is a saved state to restore,
        //so you do not need to check whether the Bundle is null:
        super.onRestoreInstanceState(savedInstanceState);
        mUser.name = savedInstanceState.getString("name");
        mUser.phoneNumber = savedInstanceState.getString("phoneNumber");
        mUser.email = savedInstanceState.getString("emailAddress");
        mUser.description = savedInstanceState.getString("description");
        mUser.deliveryAddress = savedInstanceState.getString("deliveryAddress");
        mUser.imageUri = savedInstanceState.getString("imageUri");
        updateFields(mUser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    if (imageProfileUri != null)
                        imgProfile.setImageURI(imageProfileUri);
                }

                break;
            case GALLERY_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageProfileUri = data.getData();
                    if (imageProfileUri != null) {
                        imgProfile.setImageURI(imageProfileUri);
                    } else {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                    }

                }
        }
    }


    private void getProfileData() {
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        mUser = new User(sharedPref.getString("name", ""),
                sharedPref.getString("phoneNumber", ""),
                sharedPref.getString("emailAddress", ""),
                sharedPref.getString("deliveryAddress", ""),
                sharedPref.getString("description", ""),
                sharedPref.getString("imageUri", "")
        );
        updateFields(mUser);
    }

    private void setProfileData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("phoneNumber", phoneNumber.getText().toString());
        editor.putString("emailAddress", emailAddress.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.putString("deliveryAddress", deliveryAddress.getText().toString());
        try {
            if (imageProfileUri != null || !imageProfileUri.toString().equals("")) {
                imageProfileUri = saveImage(imageProfileUri, EditProfileActivity.this);
                editor.putString("imageUri", imageProfileUri.toString());
            }
        } catch (Exception e) {
            editor.putString("imageUri", "");
        }

        editor.apply();
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "profile_photo",  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            imageProfileUri = FileProvider.getUriForFile(EditProfileActivity.this,
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageProfileUri);
                            startActivityForResult(takePicture, CAMERA_CODE);
                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, GALLERY_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateFields(User u) {
        name.setText(u.name);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);
        deliveryAddress.setText(u.deliveryAddress);
        if (u.imageUri.toString().equals("")) {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            imgProfile.setImageURI(Uri.parse(u.imageUri));
        }
    }

    /*
    This function copy a file given its URI and the Activity into the app internal storage and returns the new URI
     */
    private Uri saveImage(Uri uriFrom, Activity activity) {
        File outFile = null;
        FileOutputStream out = null;
        Bitmap bitmap = null;
        Uri fileUri = null;
        try {
            outFile = createImageFile();
            InputStream image_stream = activity.getContentResolver().openInputStream(uriFrom);
            bitmap = BitmapFactory.decodeStream(image_stream);
            out = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            fileUri = FileProvider.getUriForFile(activity,
                    BuildConfig.APPLICATION_ID,
                    outFile);
        } catch (IOException ex) {
            Log.d("MAD-APP", "IO Exception raised during File creation");
            return null;
        }
        return fileUri;
    }

    public void zoomImage() {
        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, PhotoZoomActivity.class);
        Log.d("MADAPP", mUser.imageUri);
        intent.putExtra("imageUri", mUser.imageUri);
        intent.putExtra("className", this.getClass().getName());
        // Get the transition name from the string
        String transitionName = getString(R.string.transition_zoom);

        // Define the view that the animation will start from
        View viewStart = findViewById(R.id.editprofile_imgprofile);

        ActivityOptions options =

                ActivityOptions.makeSceneTransitionAnimation(this,
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        startActivity(intent, options.toBundle());
        //Activity.startActivity(this, intent,  options.toBundle());
    }

}
