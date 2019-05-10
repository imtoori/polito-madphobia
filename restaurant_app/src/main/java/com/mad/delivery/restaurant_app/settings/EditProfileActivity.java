package com.mad.delivery.restaurant_app.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.BuildConfig;
import com.mad.delivery.restaurant_app.Database;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class EditProfileActivity extends AppCompatActivity {
    Menu menu;
    FirebaseUser currentUser;
    Restaurant mUser;
    FloatingActionButton btnCamera;
    EditText name;
    EditText phoneNumber;
    EditText emailAddress;
    EditText description;
    EditText road;
    EditText houseNumber;
    EditText doorPhone;
    EditText postCode;
    EditText city;
    EditText openingTime;
    EditText deliveryCost, minOrder;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    private FirebaseAuth mAuth;
    private ChipGroup chipGroup;
    private Set<String> categories;
    private Set<String> myCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageProfileUri =Uri.EMPTY;
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
        categories = new HashSet<>();
        myCategories = new HashSet<>();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

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

        CompoundButton.OnCheckedChangeListener filterChipListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Chip chip = (Chip) buttonView;
                if(isChecked) {
                    categories.add(chip.getText().toString().toLowerCase());
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    myCategories.add(chip.getText().toString().toLowerCase());
                } else {
                    categories.removeIf(c -> c.equals(chip.getText().toString().toLowerCase()));
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    myCategories.remove(chip.getText().toString().toLowerCase());
                }

            }
        };

        Database.getInstance().getCategories(currentUser.getUid(), set -> {
            myCategories = new HashSet<>(set);
        });
        Database.getInstance().getCategories(set -> {
            set.stream().forEach(n -> {
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
                if(myCategories.contains(n.toLowerCase())) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                }
                chip.setOnCheckedChangeListener(filterChipListener);
                chipGroup.addView(chip);
            });
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
        mUser = new Restaurant();
        Log.d("MADAPP", "SavedInstanceState contains data");
        mUser.name = savedInstanceState.getString("name");
        mUser.phoneNumber = savedInstanceState.getString("phoneNumber");
        mUser.email = savedInstanceState.getString("emailAddress");
        mUser.description = savedInstanceState.getString("description");
        mUser.road = savedInstanceState.getString("road");
        mUser.openingHours = savedInstanceState.getString("openingTime");
        mUser.houseNumber = savedInstanceState.getString("houseNumber");
        mUser.doorPhone = savedInstanceState.getString("doorPhone");
        mUser.postCode = savedInstanceState.getString("postCode");
        mUser.city = savedInstanceState.getString("city");
        mUser.imageUri = savedInstanceState.getString("imageUri");
        mUser.deliveryCost = Double.valueOf(savedInstanceState.getString("deliveryCost"));
        mUser.minOrderCost = Double.valueOf(savedInstanceState.getString("minOrder"));
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
        Database.getInstance().getRestaurantProfile(new FireBaseCallBack<Restaurant>(){
            @Override
            public void onCallback(Restaurant user) {
                if(user!=null){
                    mUser=new Restaurant(user);

                    updateFields(mUser);
                }

            }

            @Override
            public void onCallbackList(List<Restaurant> list) {

            }
        });
    }

    private void setProfileData() {

        Restaurant user  = new Restaurant(name.getText().toString(),
                emailAddress.getText().toString(),
                description.getText().toString(),
                phoneNumber.getText().toString(),
                road.getText().toString(),
                houseNumber.getText().toString(),
                doorPhone.getText().toString(),
                postCode.getText().toString(),
                city.getText().toString(),
                imageProfileUri.toString(),
                imageProfileUri.getLastPathSegment(),
                openingTime.getText().toString());
        user.id = mAuth.getCurrentUser().getUid();
        user.previewInfo.id = user.id;
        user.deliveryCost = Double.valueOf(deliveryCost.getText().toString());
        user.minOrderCost = Double.valueOf(minOrder.getText().toString());
        user.previewInfo.deliveryCost = user.deliveryCost;
        user.previewInfo.minOrderCost = user.minOrderCost;

        for(String c : myCategories) {
            user.categories.put(c.toLowerCase(), true);

        }
        Database.getInstance().putRestaurantIntoCategory(user.id, myCategories);
       /* Database.getInstance().getMenuItems(new OnDataFetched<List<MenuItemRest>, String>() {
            @Override
            public void onDataFetched(List<MenuItemRest> data) {
                data.forEach(m->{
                    user.menuItems.put(m.id,m);
                });
            }

            @Override
            public void onError(String error) {

            }
        });*/
       user.menuItems =mUser.menuItems;
        Database.getInstance().putRestaurantProfile(user);

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

    private void updateFields(Restaurant u) {
        Log.d("UPDATE","name: " + u.name);
        name.setText(u.name);
        phoneNumber.setText(u.phoneNumber);
        emailAddress.setText(u.email);
        description.setText(u.description);
        road.setText(u.road);
        openingTime.setText(u.openingHours);
        houseNumber.setText(u.houseNumber);
        doorPhone.setText(u.doorPhone);
        postCode.setText(String.valueOf(u.postCode));
        city.setText(u.city);
        deliveryCost.setText(String.valueOf(u.deliveryCost));
        minOrder.setText(String.valueOf(u.minOrderCost));
        imageProfileUri = Uri.parse( mUser.imageUri);
        imgProfile.setImageURI(Uri.parse(u.imageUri));

        if(imgProfile.getDrawable() == null) {
            Database.getInstance().getImage(u.imageName,"/images/profile/", new FireBaseCallBack<Uri>() {
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

                @Override
                public void onCallbackList(List<Uri> list) {

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
        String nameString = "[a-zA-Z0-9\'\\s]+";
        String phoneNumberString = "^\\+?(?:[0-9] ?){6,14}[0-9]$";
        String postalCodeString = "[0-9]{5}";
        String numberString = "[1-9][0-9]*";
        String roadString = "([A-Za-z0-9'-_\\s])+";
        String cityString = "([A-Za-z\'\\s-])+";
        String doorPhoneString = "([A-Za-z0-9\'\\s-])+";
        String deliveryAndOrder = "[0-9.,]{1,5}";

        if(!deliveryCost.getText().toString().matches(deliveryAndOrder)) {
            deliveryCost.setError(getResources().getString(R.string.check_delivery));
            result = false;
        }
        if(!minOrder.getText().toString().matches(deliveryAndOrder)) {
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



}