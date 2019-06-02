package com.mad.delivery.restaurant_app.settings;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.GPSTracker;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.BuildConfig;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.joda.time.DateTime;

public class EditProfileActivity extends AppCompatActivity {
    Menu menu;
    Restaurant restaurant;
    FloatingActionButton btnCamera;
    EditText name, phoneNumber, emailAddress, description, road, houseNumber, doorPhone, postCode, city, openingTime;
    EditText deliveryCost, minOrder;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileLocalUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    private FirebaseAuth mAuth;
    private ChipGroup chipGroup;
    private CompoundButton.OnCheckedChangeListener filterChipListener;
    private Uri imageLink;
    ImageView getposition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageProfileLocalUri = Uri.EMPTY;
        setContentView(R.layout.activity_editprofile);

        myToolbar = findViewById(R.id.editProfileToolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.editprofile_name);
        phoneNumber = findViewById(R.id.editprofile_phone);
        emailAddress = findViewById(R.id.editprofile_email);
        description = findViewById(R.id.editprofile_description);
        road = findViewById(R.id.editprofile_road);
        houseNumber = findViewById(R.id.editprofile_housenumber);
        doorPhone = findViewById(R.id.editprofile_doorphone);
        postCode = findViewById(R.id.editprofile_postalcode);
        city = findViewById(R.id.editprofile_city);
        imgProfile = findViewById(R.id.editprofile_imgprofile);
        openingTime = findViewById(R.id.openinghours_et);
        btnCamera = findViewById(R.id.editprofile_btncamera);
        chipGroup = findViewById(R.id.chip_group);
        deliveryCost = findViewById(R.id.et_delivery_fee);
        minOrder = findViewById(R.id.et_min_order);
        getposition = findViewById(R.id.img_position);
        getposition.setOnClickListener( v -> {
            GPSTracker gps = new GPSTracker(this);
            if (gps.isGPSEnabled) {
                Geocoder geocoder;
                List<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    Double latitude = gps.getLatitude();
                    Double longitude = gps.getLongitude();

                    addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    city.setText(addresses.get(0).getLocality());
                    road.setText(addresses.get(0).getThoroughfare());
                    houseNumber.setText(addresses.get(0).getFeatureName());
                    //String country = addresses.get(0).getCountryCode();
                    postCode.setText(addresses.get(0).getPostalCode());

                } else {
                    Toast.makeText(this, "Your address isn't found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Your gps is disabled", Toast.LENGTH_SHORT).show();
            }
        });
        btnCamera.setOnClickListener(view -> {
            selectImage(EditProfileActivity.this);
        });
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        restaurant = new Restaurant();
        Bundle bundle = getIntent().getExtras();
        restaurant = (Restaurant) bundle.get("user");
        if(restaurant.categories == null) restaurant.categories = new HashMap<>();
        imageLink = (Uri) bundle.get("imageLink");
        Log.d("MADAPP", restaurant.toString());
        RestaurantDatabase.getInstance().getCategories(innerSet -> {
            innerSet.stream().forEach(n -> {
                Chip chip = new Chip(this);
                chip.setText(n);
                chip.setChipBackgroundColorResource(R.color.colorWhite);
                chip.setChipStrokeWidth((float) 0.1);
                chip.setChipStrokeColorResource(R.color.colorPrimary);
                chip.setCheckable(true);
                chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                chip.setRippleColorResource(R.color.colorPrimaryDark);
                chip.setCheckedIconVisible(false);
                chip.setChipIconTintResource(R.color.colorPrimary);
                Log.d("MADAPP", restaurant.categories.toString());
                if(restaurant.categories != null) {
                    if (restaurant.categories.containsKey(n.toLowerCase())) {
                        chip.setChecked(true);
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                        chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                        chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    }
                }
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    Chip ch = (Chip) buttonView;
                    if (isChecked) {
                        ch.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                        ch.setTextColor(getResources().getColor(R.color.colorWhite, null));
                        ch.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                        restaurant.categories.put(ch.getText().toString().toLowerCase(), true);
                    } else {
                        ch.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                        ch.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                        ch.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                        restaurant.categories.remove(ch.getText().toString().toLowerCase());
                    }
                });
                chipGroup.addView(chip);
            });
        });

        getProfileData();
        if(!checkPermissions()) {
            requestPermissions();
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
                if (checkConstraints()) {

                    try {
                        setProfileData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
        outState.putString("phoneNumber", phoneNumber.getText().toString());
        outState.putString("emailAddress", emailAddress.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("road", road.getText().toString());
        outState.putString("houseNumber", houseNumber.getText().toString());
        outState.putString("doorPhone", doorPhone.getText().toString());
        outState.putString("postCode", postCode.getText().toString());
        outState.putString("city", city.getText().toString());
        outState.putString("openingTime", openingTime.getText().toString());
        outState.putString("deliveryCost", deliveryCost.getText().toString());
        outState.putString("minOrder", minOrder.getText().toString());
        if (imageProfileLocalUri != Uri.EMPTY)
            outState.putString("imageUri", imageProfileLocalUri.toString());
        else
            outState.putString("imageUri", "");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //The system calls onRestoreInstanceState(), after the onStart() method,
        //only if there is a saved state to restore,
        //so you do not need to check whether the Bundle is null:
        super.onRestoreInstanceState(savedInstanceState);
        restaurant = new Restaurant();
        Log.d("MADAPP", "SavedInstanceState contains data");
        restaurant.previewInfo.name = savedInstanceState.getString("name");
        restaurant.phoneNumber = savedInstanceState.getString("phoneNumber");
        restaurant.email = savedInstanceState.getString("emailAddress");
        restaurant.previewInfo.description = savedInstanceState.getString("description");
        restaurant.road = savedInstanceState.getString("road");
        restaurant.openingHours = savedInstanceState.getString("openingTime");
        restaurant.houseNumber = savedInstanceState.getString("houseNumber");
        restaurant.doorPhone = savedInstanceState.getString("doorPhone");
        restaurant.postCode = savedInstanceState.getString("postCode");
        restaurant.city = savedInstanceState.getString("city");
        imageProfileLocalUri = Uri.parse(savedInstanceState.getString("imageUri"));
        restaurant.previewInfo.deliveryCost = Double.valueOf(savedInstanceState.getString("deliveryCost"));
        restaurant.previewInfo.minOrderCost = Double.valueOf(savedInstanceState.getString("minOrder"));
        updateFields(restaurant);
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
        updateFields(restaurant);
    }

    private void setProfileData() throws IOException {
        if (restaurant.previewInfo == null) restaurant.previewInfo = new PreviewInfo();
        restaurant.previewInfo.name = name.getText().toString();
        restaurant.email = emailAddress.getText().toString();
        restaurant.previewInfo.description = description.getText().toString();
        restaurant.phoneNumber = phoneNumber.getText().toString();
        restaurant.road = road.getText().toString();
        restaurant.houseNumber = houseNumber.getText().toString();
        restaurant.doorPhone = doorPhone.getText().toString();
        restaurant.postCode = postCode.getText().toString();
        restaurant.city = city.getText().toString();
        if (!imageProfileLocalUri.equals(Uri.EMPTY)) {
            restaurant.previewInfo.imageName = imageProfileLocalUri.getLastPathSegment();
        }

        restaurant.openingHours = openingTime.getText().toString();
        restaurant.previewInfo.deliveryCost = Double.valueOf(deliveryCost.getText().toString());
        restaurant.previewInfo.minOrderCost = Double.valueOf(minOrder.getText().toString());
        if (restaurant.categories == null) restaurant.categories = new HashMap<>();
        //Transform address in latitude and longitude
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(road.getText().toString() + houseNumber.getText().toString() + city.getText().toString(), 1);
        if(addresses.size() > 0) {
            Log.d("Address1: ",road.getText().toString() + houseNumber.getText().toString() + city.getText().toString());

            Double latitude= addresses.get(0).getLatitude();
            Double longitude= addresses.get(0).getLongitude();
            Log.d("Address2: ",latitude.toString()+" "+longitude.toString());

            restaurant.latitude=latitude;
            restaurant.longitude=longitude;
        }

        RestaurantDatabase.getInstance().updateRestaurantProfile(restaurant, imageProfileLocalUri);
        RestaurantDatabase.getInstance().putRestaurantIntoCategory(restaurant.previewInfo.id, restaurant.categories.keySet());
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

    private void updateFields(Restaurant u) {
        name.setText(u.previewInfo.name);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.previewInfo.description);
        road.setText(u.road);
        openingTime.setText(u.openingHours);
        houseNumber.setText(u.houseNumber);
        doorPhone.setText(u.doorPhone);
        postCode.setText(String.valueOf(u.postCode));
        city.setText(u.city);
        deliveryCost.setText(String.valueOf(u.previewInfo.deliveryCost));
        minOrder.setText(String.valueOf(u.previewInfo.minOrderCost));
        if (u.previewInfo != null && u.previewInfo.imageName != null && !u.previewInfo.imageName.equals("")) {
            if (imageProfileLocalUri == null || imageProfileLocalUri == Uri.EMPTY || imageProfileLocalUri.equals(Uri.EMPTY)) {
                if(imageLink != null) {
                    Picasso.get().load(imageLink.toString()).into(imgProfile);
                    return;
                }
                RestaurantDatabase.getInstance().downloadImage(u.previewInfo.id, "profile", u.previewInfo.imageName, imageUri -> {
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

    public boolean checkConstraints() {
        boolean result = true;
        String nameString = "[a-zA-Z0-9\'\\s]+";
        String phoneNumberString = "^\\+?(?:[0-9] ?){6,14}[0-9]$";
        String postalCodeString = "[0-9]{5}";
        String numberString = "[1-9][0-9]*";
        String roadString = "([A-Za-z0-9'-_\\s])+";
        String cityString = "([A-Za-z\'\\s-])+";
        String doorPhoneString = "([A-Za-z0-9\'\\s-])+";
        String deliveryAndOrder = "[0-9.,]{1,5}";

        if (!deliveryCost.getText().toString().matches(deliveryAndOrder)) {
            deliveryCost.setError(getResources().getString(R.string.check_delivery));
            result = false;
        }
        if (!minOrder.getText().toString().matches(deliveryAndOrder)) {
            minOrder.setError(getResources().getString(R.string.check_minorder));
            result = false;
        }

        if (!name.getText().toString().matches(nameString)) {
            name.setError(getResources().getString(R.string.check_name));
            result = false;
        }

        if (!road.getText().toString().matches(roadString)) {
            road.setError(getResources().getString(R.string.check_road));
            result = false;
        }

        if (!city.getText().toString().matches(cityString)) {
            city.setError(getResources().getString(R.string.check_city));
            result = false;
        }

        if (!phoneNumber.getText().toString().matches(phoneNumberString)) {
            phoneNumber.setError(getResources().getString(R.string.check_phone));
            result = false;
        }

        if (!doorPhone.getText().toString().matches(doorPhoneString)) {
            doorPhone.setError(getResources().getString(R.string.check_doorphone));
            result = false;

        }

        if (!postCode.getText().toString().matches(postalCodeString)) {
            postCode.setError(getResources().getString(R.string.check_postCode));
            result = false;
        }

        if (!houseNumber.getText().toString().matches(numberString)) {
            houseNumber.setError(getResources().getString(R.string.house_number));
            result = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches()) {
            emailAddress.setError(getResources().getString(R.string.email));
            result = false;
        }
        return result;
    }

    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_CODE:
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!camera) {
                    showMessageOKCancel();
                }
                break;
            default:
                break;

        }

    }

    private void showMessageOKCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        View root = getLayoutInflater().inflate(R.layout.dialog_ok_cancel, null);
        builder.setView(root);
        Button btnUnderstand = root.findViewById(R.id.btn_continue);
        Button btnChange = root.findViewById(R.id.btn_grant);
        AlertDialog dialog = builder.create();
        btnUnderstand.setOnClickListener( v -> {
            btnCamera.setVisibility(View.GONE);
            dialog.dismiss();
        });
        btnChange.setOnClickListener( v -> {
            requestPermissions();
            dialog.dismiss();
        });
        dialog.show();
    }




}