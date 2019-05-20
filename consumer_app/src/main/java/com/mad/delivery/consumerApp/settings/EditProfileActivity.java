package com.mad.delivery.consumerApp.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.consumerApp.BuildConfig;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.firebaseCallback;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class EditProfileActivity extends AppCompatActivity {
    Menu menu;
    User user;
    private FirebaseAuth mAuth;
    FloatingActionButton btnCamera;
    EditText name;
    EditText lastName;
    EditText phoneNumber;
    EditText emailAddress;
    EditText description;
    EditText road;
    EditText houseNumber;
    EditText doorPhone;
    EditText postCode;
    EditText city;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileLocalUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    private Uri imageLink;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imageProfileLocalUri = Uri.EMPTY;
        myToolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        setSupportActionBar(myToolbar);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = findViewById(R.id.editprofile_name);
        lastName = findViewById(R.id.editprofile_lastname);
        phoneNumber = findViewById(R.id.editprofile_phone);
        emailAddress = findViewById(R.id.editprofile_email);
        description = findViewById(R.id.editprofile_description);
        road = findViewById(R.id.editprofile_road);
        houseNumber = findViewById(R.id.editprofile_housenumber);
        doorPhone = findViewById(R.id.editprofile_doorphone);
        postCode = findViewById(R.id.editprofile_postalcode);
        city = findViewById(R.id.editprofile_city);
        imgProfile = findViewById(R.id.editprofile_imgprofile);
        btnCamera = findViewById(R.id.editprofile_btncamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(EditProfileActivity.this);
            }
        });

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
                @Override
                public void onSuccess(User u) {
                }

                @Override
                public void onFailure() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
        user = new User();

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.get("user");
        imageLink = (Uri) bundle.get("imageLink");
        Log.d("MADAPP", user.toString());
        getProfileData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
            case R.id.edit_profile_done:
                if(checkConstraints()) {
                    setProfileData();
                    Toast.makeText(this, "Your profile has been saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // invoked when the activity may be temporarily destroyed, save the instance state here
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("lastName", lastName.getText().toString());
        outState.putString("phoneNumber", phoneNumber.getText().toString());
        outState.putString("emailAddress", emailAddress.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("road", road.getText().toString());
        outState.putString("houseNumber", houseNumber.getText().toString());
        outState.putString("doorPhone", doorPhone.getText().toString());
        outState.putString("postCode", postCode.getText().toString());
        outState.putString("city", city.getText().toString());

        if (imageProfileLocalUri != Uri.EMPTY)
            outState.putString("imageUri", imageProfileLocalUri.toString());
        else
            outState.putString("imageUri", "");
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //The system calls onRestoreInstanceState(), after the onStart() method,
        //only if there is a saved state to restore,
        //so you do not need to check whether the Bundle is null:
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MADAPP", "SavedInstanceState contains data");
        user.name = savedInstanceState.getString("name");
        user.lastName = savedInstanceState.getString("lastName");
        user.phoneNumber = savedInstanceState.getString("phoneNumber");
        user.email = savedInstanceState.getString("emailAddress");
        user.description = savedInstanceState.getString("description");
        user.road = savedInstanceState.getString("road");
        user.houseNumber = savedInstanceState.getString("houseNumber");
        user.doorPhone = savedInstanceState.getString("doorPhone");
        user.postCode = savedInstanceState.getString("postCode");
        user.city = savedInstanceState.getString("city");
        user.imageUri = savedInstanceState.getString("imageUri");
        updateFields(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Log.d("MADAPP", "imageProfileLocalUri on ActivityResult: " + imageProfileLocalUri.toString());
                    imageProfileLocalUri = saveImage(imageProfileLocalUri, EditProfileActivity.this);
                    imgProfile.setImageURI(imageProfileLocalUri);
                }
                break;
            case GALLERY_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageProfileLocalUri = data.getData();
                    if (imageProfileLocalUri != null) {
                        imageProfileLocalUri = saveImage(imageProfileLocalUri, EditProfileActivity.this);
                        imgProfile.setImageURI(imageProfileLocalUri);
                    } else {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                    }

                }
        }
    }


    private void getProfileData() {
        updateFields(user);
    }

    private void setProfileData() {
        user.name = name.getText().toString();
        user.lastName = lastName.getText().toString();
        user.phoneNumber = phoneNumber.getText().toString();
        user.email =  emailAddress.getText().toString();
        user.description = description.getText().toString();
        user.road = road.getText().toString();
        user.houseNumber = houseNumber.getText().toString();
        user.doorPhone = doorPhone.getText().toString();
        user.postCode = postCode.getText().toString();
        user.city = city.getText().toString();
        if (!imageProfileLocalUri.equals(Uri.EMPTY)) {
            user.imageName = imageProfileLocalUri.getLastPathSegment();
        }
        ConsumerDatabase.getInstance().updateUserProfile(user, imageProfileLocalUri);
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
                            imageProfileLocalUri = FileProvider.getUriForFile(EditProfileActivity.this,
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageProfileLocalUri);
                            startActivityForResult(takePicture, CAMERA_CODE);
                        }
                    } else {
                        Log.d("MADAPP", "Camera cancel key..");
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
        lastName.setText(u.lastName);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);
        road.setText(u.road);
        houseNumber.setText(u.houseNumber);
        doorPhone.setText(u.doorPhone);
        postCode.setText(String.valueOf(u.postCode));
        city.setText(u.city);

        if (u.imageName != null && !u.imageName.equals("")) {
            if (imageProfileLocalUri == null || imageProfileLocalUri == Uri.EMPTY || imageProfileLocalUri.equals(Uri.EMPTY)) {
                if(imageLink != null) {
                    Picasso.get().load(imageLink.toString()).into(imgProfile);
                    return;
                }
                ConsumerDatabase.getInstance().downloadImage(u.id, "profile", u.imageName, imageUri -> {
                    if (imageUri == null || imageUri.equals(Uri.EMPTY)) {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                        imageProfileLocalUri = Uri.EMPTY;
                    } else {
                        Picasso.get().load(imageUri).into(imgProfile);
                    }
                });
            } else {
                Picasso.get().load(imageProfileLocalUri.toString()).into(imgProfile);
            }
        } else {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        }

    }

    /*
    This function copy a file given its URI and the Activity into the app internal storage and returns the new URI
     */
    private Uri saveImage(Uri uriFrom, Activity activity) {
        File outFile;
        FileOutputStream out;
        Bitmap bitmap;
        Uri fileUri;
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

    public boolean checkConstraints(){
        boolean result = true;
        String nameString =  "[a-zA-Z]+";
        String lastNameString = "([a-zA-Z]+([ '-][a-zA-Z]+)*)|([A-Z][a-z]*)";
        String phoneNumberString = "^\\+?(?:[0-9] ?){6,14}[0-9]$";
        String postalCodeString= "[0-9]{5}";
        String numberString = "[1-9][0-9]*";
        String roadString = "([A-Za-z0-9'-_\\s])+";
        String cityString = "([A-Za-z\'\\s-])+";
        String doorPhoneString = "([A-Za-z0-9\'\\s-])+";

        if(!name.getText().toString().matches(nameString)){
            name.setError(getResources().getString(R.string.check_name));
            result = false;
        }

        if(!road.getText().toString().matches(roadString)) {
            road.setError(getResources().getString(R.string.check_road));
            result = false;
        }

        if(!city.getText().toString().matches(cityString)){
            city.setError(getResources().getString(R.string.check_city));
            result = false;
        }

        if(!lastName.getText().toString().matches(lastNameString)){
            lastName.setError(getResources().getString(R.string.check_lastName));
            result = false;
        }


        if(!phoneNumber.getText().toString().matches(phoneNumberString)){
            phoneNumber.setError(getResources().getString(R.string.check_phone));
            result = false;
        }

        if(!doorPhone.getText().toString().matches(doorPhoneString)){
            doorPhone.setError(getResources().getString(R.string.check_doorphone));
            result = false;

        }

        if(!postCode.getText().toString().matches(postalCodeString)){
            postCode.setError(getResources().getString(R.string.check_postCode));
            result = false;
        }

        if(!houseNumber.getText().toString().matches(numberString)){
            houseNumber.setError(getResources().getString(R.string.house_number));
            result = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches()) {
            emailAddress.setError(getResources().getString(R.string.email));
            result = false;
        }
        return result;
    }
}