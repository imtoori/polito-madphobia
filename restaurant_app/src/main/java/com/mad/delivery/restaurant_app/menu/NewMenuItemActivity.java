package com.mad.delivery.restaurant_app.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.BuildConfig;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewMenuItemActivity extends AppCompatActivity {
    Menu menu;
    FloatingActionButton btnCamera;
    MenuItemRest menuItem;
    EditText name, price, availability, description;
    AutoCompleteTextView category;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageMenuItemUri, imageLink;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    List<String> subItems = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Restaurant restaurant;
    private List<String> categories;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmenuitem);
        imageMenuItemUri = Uri.EMPTY;
        imageLink = Uri.EMPTY;
        myToolbar = findViewById(R.id.newmenuitem_toolbar);
        setTitle("Create a new Dish");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        imgProfile = findViewById(R.id.newmenuitem_imgprofile);
        name = findViewById(R.id.newdish_name);
        price = findViewById(R.id.newdish_price);
        description = findViewById(R.id.newdish_desc);
        category = findViewById(R.id.newdish_category);
        availability = findViewById(R.id.newdish_availability);
        btnCamera = findViewById(R.id.newmenu_btncamera);
        btnCamera.setOnClickListener(view -> {
                selectImage(NewMenuItemActivity.this);
        });
        categories = new ArrayList<>(getIntent().getStringArrayListExtra("categories"));
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, categories);
        category.setAdapter(adapter);
        category.setThreshold(1);
        menuItem = getIntent().getParcelableExtra("item");

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
        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant user) {
                restaurant = user;
                if(menuItem != null) {
                    updateFields(menuItem);
                } else {
                    menuItem = new MenuItemRest();
                }
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        menuItem.name = savedInstanceState.getString("name");
        menuItem.description = savedInstanceState.getString("description");
        menuItem.price = Double.parseDouble(savedInstanceState.getString("price"));
        menuItem.category = savedInstanceState.getString("category");
        menuItem.availability = Integer.parseInt(savedInstanceState.getString("availability"));
        //  menuItem.imageUri = Uri.EMPTY;
        //menuItem.subItems = savedInstanceState.getStringArrayList("subitems");
        updateFields(menuItem);

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageMenuItemUri = saveImage(imageMenuItemUri, NewMenuItemActivity.this);
                    imgProfile.setImageURI(imageMenuItemUri);
                }
                break;
            case GALLERY_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageMenuItemUri = data.getData();
                    if (imageMenuItemUri != null) {
                        imageMenuItemUri = saveImage(imageMenuItemUri, NewMenuItemActivity.this);
                        imgProfile.setImageURI(imageMenuItemUri);
                    } else {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
                    }
                }
        }
    }


    protected void onSaveInstanceState(Bundle outState) {
        // invoked when the activity may be temporarily destroyed, save the instance state here
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("price", price.getText().toString());
        outState.putString("availability", availability.getText().toString());
        outState.putString("category", category.getText().toString());
    }

    private void updateFields(MenuItemRest u) {
        name.setText(u.name);
        description.setText(u.description);
        price.setText(String.valueOf(u.price));
        availability.setText(String.valueOf(u.availability));
        //loadSubItems(u.subItems);
        category.setText(u.category);

        if (u.imageName != null && !u.imageName.equals("")) {
            if (imageMenuItemUri == null || imageMenuItemUri == Uri.EMPTY || imageMenuItemUri.equals(Uri.EMPTY)) {
                if(imageLink != null && !imageLink.equals(Uri.EMPTY)) {
                    Picasso.get().load(imageLink.toString()).into(imgProfile);
                    return;
                }
                RestaurantDatabase.getInstance().downloadImage(restaurant.previewInfo.id, "menu", u.imageName, imageUri -> {
                    if (imageUri == null || imageUri.equals(Uri.EMPTY)) {
                        imgProfile.setImageDrawable(getDrawable(R.drawable.restaurant_default));
                        imageMenuItemUri = Uri.EMPTY;
                    } else {
                        Picasso.get().load(imageUri).into(imgProfile);
                    }
                });
            } else {
                Picasso.get().load(imageMenuItemUri.toString()).into(imgProfile);
            }
        } else {
            imgProfile.setImageDrawable(getDrawable(R.drawable.restaurant_default));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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


    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose dish picture");
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
                            imageMenuItemUri = FileProvider.getUriForFile(NewMenuItemActivity.this,
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);

                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageMenuItemUri);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_done:
                if (checkConstraints()) {
                    setMenuItem();
                    Toast.makeText(this, "Dish has been saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setMenuItem() {
        menuItem.restaurantId = currentUser.getUid();
        menuItem.name = name.getText().toString();
        menuItem.category = category.getText().toString();
        menuItem.description = description.getText().toString();
        menuItem.availability = Integer.parseInt(availability.getText().toString());
        menuItem.price = Double.parseDouble(price.getText().toString());
        RestaurantDatabase.getInstance().updateMenuItem(menuItem.restaurantId, menuItem, imageMenuItemUri);
    }

    public boolean checkConstraints() {
        boolean result = true;
        String nameString = "[\\s\\w]+";
        String priceString = "([0-9][0-9]*)|([0-9][0-9]*\\.[0-9][0-9]*)";
        String timeString = "([0-9][0-9]*)";

        if (name.getText().toString().length() <= 0) {
            name.setError(getResources().getString(R.string.check_name_dish));
            result = false;
        }

        if (description.getText().toString().length() <= 0) {
            description.setError(getResources().getString(R.string.check_description));
            result = false;
        }

        if (!price.getText().toString().matches(priceString)) {
            price.setError(getResources().getString(R.string.check_price));
            result = false;
        }


        if (!category.getText().toString().matches(nameString)) {
            category.setError(getResources().getString(R.string.check_category));
            result = false;

        }
        if (!availability.getText().toString().matches(timeString)) {
            availability.setError(getResources().getString(R.string.check_availability));
            result = false;

        }

        return result;
    }


}