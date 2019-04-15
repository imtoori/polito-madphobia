package com.mad.delivery.restaurant_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class NewMenuItemActivity  extends AppCompatActivity {

    SharedPreferences sharedPref;
    Menu menu;
    FloatingActionButton btnCamera;
    MenuItemRest menuItem;
    EditText name;
    EditText price;
    EditText time;
    EditText category;
    EditText availability;

    EditText description;
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileUri;
    String currentPhotoPath;
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;

    Integer index ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmenuitem);
        imageProfileUri = Uri.EMPTY;
        myToolbar = (Toolbar) findViewById(R.id.newmenuitem_toolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imgProfile = findViewById(R.id.newmenuitem_imgprofile);
        name = findViewById(R.id.newmenuitem_name);
        price = findViewById(R.id.newmenuitem_price);
        time = findViewById(R.id.newmenuitem_time);
        description = findViewById(R.id.newmenuitem_description);
        imgProfile = findViewById(R.id.newmenuitem_imgprofile);
        category = findViewById(R.id.newmenuitem_category);
        availability = findViewById(R.id.newmenuitem_availability);
        btnCamera = findViewById(R.id.newmenu_btncamera);
        index =  getIntent().getIntExtra("id",-1);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(NewMenuItemActivity.this);
            }
        });

        if(index>0){
            try {
                Log.d("rr",Database.getInstance().getMenuItems().get(index).category);
                updateFields(Database.getInstance().getMenuItems().get(index));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        menuItem = new MenuItemRest();
        menuItem.name = savedInstanceState.getString("name");
        menuItem.description = savedInstanceState.getString("description");
        menuItem.price = Double.parseDouble(savedInstanceState.getString("price"));
        menuItem.ttl = Integer.parseInt( savedInstanceState.getString("time"));
        menuItem.category = savedInstanceState.getString("category");
        menuItem.availability = Integer.parseInt(savedInstanceState.getString("availability"));
        menuItem.imageUri = Uri.parse(savedInstanceState.getString("imageUri"));
        try {
            updateFields(menuItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        // invoked when the activity may be temporarily destroyed, save the instance state here
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("price", price.getText().toString());
        outState.putString("time", time.getText().toString());
        outState.putString("availability", availability.getText().toString());
        outState.putString("category", category.getText().toString());

        if (imageProfileUri != Uri.EMPTY)
            outState.putString("imageUri", imageProfileUri.toString());
        else
            outState.putString("imageUri", "");

    }

    private void updateFields(MenuItemRest u) throws IOException {
        Log.d("Update",u.name.toString());
        name.setText(u.name);
        description.setText(u.description);
        time.setText(u.ttl.toString());
        price.setText(u.price.toString());
        availability.setText(u.availability.toString());

        category.setText(u.category.toString());

        Log.d("TAG", u.category);

        //   imgProfile.setImageURI(Uri.parse(u.imgUrl));
        // imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        /*if (u.imageUri == Uri.EMPTY || u.imageUri.toString().equals("")) {
            Log.d("MADAPP", "Setting user default image");
            imageProfileUri = Uri.EMPTY;
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            Log.d("MADAPP", "Setting custom user image");
            imageProfileUri = u.imageUri;
            imgProfile.setImageURI(u.imageUri);
        }*/
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
                            imageProfileUri = FileProvider.getUriForFile(NewMenuItemActivity.this, BuildConfig.APPLICATION_ID, photoFile);
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

                    if(index==-1) {
                        Database.getInstance().addMenuItems(name.getText().toString(), description.getText().toString(), category.getText().toString(),price.getText().toString(), availability.getText().toString(),time.getText().toString(), imageProfileUri.toString());

                    }
                    else{
                        Database.getInstance().setMenuItems(index,name.getText().toString(), category.getText().toString(),description.getText().toString(), price.getText().toString(),availability.getText().toString(), time.getText().toString(), imageProfileUri.toString());

                    }

                    Toast.makeText(this, "Dish has been saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean checkConstraints() {
        return true;
    }
}
