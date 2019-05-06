package com.mad.delivery.bikerApp.settings;

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
import com.mad.delivery.bikerApp.BuildConfig;
import com.mad.delivery.bikerApp.Database;
import com.mad.delivery.bikerApp.FirebaseCallbackItem;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.resources.Biker;
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
    SharedPreferences sharedPref;
    Menu menu;
    Biker mUser;
    FloatingActionButton btnCamera;
    EditText name;
    EditText lastname;
    EditText phoneNumber;
    EditText emailAddress;
    EditText description;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        imageProfileUri = Uri.EMPTY;
        myToolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = findViewById(R.id.editprofile_name);
        lastname = findViewById(R.id.editprofile_lastname);
        phoneNumber = findViewById(R.id.editprofile_phone);
        emailAddress = findViewById(R.id.editprofile_email);
        description = findViewById(R.id.editprofile_description);

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
        } else {
            getProfileData();
        }
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
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
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
                if (checkConstraints()) {
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
        outState.putString("lastname", lastname.getText().toString());
        outState.putString("phoneNumber", phoneNumber.getText().toString());
        outState.putString("emailAddress", emailAddress.getText().toString());
        outState.putString("description", description.getText().toString());
        if (imageProfileUri != Uri.EMPTY)
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
        mUser = new Biker();
        Log.d("MADAPP", "SavedInstanceState contains data");
        mUser.name = savedInstanceState.getString("name");
        mUser.lastname = savedInstanceState.getString("lastname");
        mUser.phoneNumber = savedInstanceState.getString("phoneNumber");
        mUser.email = savedInstanceState.getString("emailAddress");
        mUser.description = savedInstanceState.getString("description");
        mUser.imageUri = savedInstanceState.getString("imageUri");
        updateFields(mUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Log.d("MADAPP", "imageProfileUri on ActivityResult: " + imageProfileUri.toString());
                    if (imageProfileUri != null)
                        Log.d("MADAPP", "i am here");
                    imageProfileUri = saveImage(imageProfileUri, EditProfileActivity.this);
                    imgProfile.setImageURI(imageProfileUri);
                }
                break;
            case GALLERY_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageProfileUri = data.getData();
                    if (imageProfileUri != null) {
                        imageProfileUri = saveImage(imageProfileUri, EditProfileActivity.this);
                        imgProfile.setImageURI(imageProfileUri);
                    } else {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                    }

                }
        }
    }


    private void getProfileData() {

        Database.getInstance().getBikerProfile(new FirebaseCallbackItem<Biker>(){
            @Override
            public void onCallback(Biker user) {
                if(user!=null){
                    mUser=new Biker(user);
                    updateFields(mUser);
                }

            }
        });
    }

    private void setProfileData() {
        Biker user  = new Biker(name.getText().toString(),
                lastname.getText().toString(),
                phoneNumber.getText().toString(),
                emailAddress.getText().toString(),
                description.getText().toString(),
                imageProfileUri);
        Database.getInstance().putBikerProfile(user);
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

    private void updateFields(Biker u) {
        name.setText(u.name);
        lastname.setText(u.lastname);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);

        imageProfileUri = Uri.parse( mUser.imageUri);
        imgProfile.setImageURI(Uri.parse(u.imageUri));

        if(imgProfile.getDrawable() == null) {
            Database.getInstance().getImage(u.imageName, "/images/profile/", new FirebaseCallbackItem<Uri>() {
                @Override
                public void onCallback(Uri item) {
                    if (item != null) {
                        if (item == Uri.EMPTY || item.toString().equals("")) {
                            Log.d("MADAPP", "Setting user default image");
                            imageProfileUri = Uri.EMPTY;

                            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                        } else {
                            Log.d("MADAPP", "Setting custom user image");
                            //  imageProfileUri = item;
                            // imgProfile.setImageURI(item);
                            Picasso.get().load(item.toString()).into(imgProfile);
                            // u.imageUri = saveImage(item,EditProfileActivity.this).toString();

                        }
                    }

                }
            });
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

    public boolean checkConstraints() {
        boolean result = true;
        String nameString = "[a-zA-Z]+";
        String phoneNumberString = "^\\+?(?:[0-9] ?){6,14}[0-9]$";
        String postalCodeString = "[0-9]{5}";
        String numberString = "[1-9][0-9]*";
        String roadString = "([A-Za-z0-9'-_\\s])+";
        String cityString = "([A-Za-z\'\\s-])+";
        String doorPhoneString = "([A-Za-z0-9\'\\s-])+";

        if (!name.getText().toString().matches(nameString)) {
            name.setError(getResources().getString(R.string.check_name));
            result = false;
        }
        if (!lastname.getText().toString().matches(nameString)) {
            lastname.setError(getResources().getString(R.string.check_lastName));
            result = false;
        }


        if (!phoneNumber.getText().toString().matches(phoneNumberString)) {
            phoneNumber.setError(getResources().getString(R.string.check_phone));
            result = false;
        }



        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches()) {
            emailAddress.setError(getResources().getString(R.string.email));
            result = false;
        }
        return result;
    }



}