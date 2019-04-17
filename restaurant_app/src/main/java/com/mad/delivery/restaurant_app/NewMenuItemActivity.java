package com.mad.delivery.restaurant_app;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

public class NewMenuItemActivity extends AppCompatActivity {

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
    List<Integer> subItems = new ArrayList<>();

    Integer index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newmenuitem);
        imageProfileUri = Uri.EMPTY;
        myToolbar = findViewById(R.id.newmenuitem_toolbar);
        setTitle("Edit menu item");
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
        index = getIntent().getIntExtra("id", -1);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(NewMenuItemActivity.this);
            }
        });

        if (index > 0) {
            Log.d("rr", Database.getInstance().getMenuItem(index).category);
            updateFields(Database.getInstance().getMenuItem(index));
            return;
        }

        Intent intent = getIntent();
        if (intent != null) {
            int[] items = intent.getIntArrayExtra("menuitems");
            if (items != null) {
                List<Integer> integers = new ArrayList<>();
                for (int item : items) {
                    integers.add(item);
                }
                loadSubItems(integers);
            }
        }

    }

    private void loadSubItems(List<Integer> items) {
        if (items != null && items.size() > 0) {
            category.setText(R.string.offer);
            category.setEnabled(false);

            CardView cardView = findViewById(R.id.items_card);
            cardView.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = findViewById(R.id.item_layout);

            for (Integer item : items) {
                this.subItems.add(item);
                MenuItemRest menuItemRest = Database.getInstance().getMenuItem(item);

                if (menuItemRest == null) {
                    continue;
                }

                TextView textView = new TextView(this);
                textView.setText(" - " + menuItemRest.name);
                linearLayout.addView(textView);
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
        menuItem.ttl = Integer.parseInt(savedInstanceState.getString("time"));
        menuItem.category = savedInstanceState.getString("category");
        menuItem.availability = Integer.parseInt(savedInstanceState.getString("availability"));
        menuItem.imageUri = Uri.parse(savedInstanceState.getString("imageUri"));
        menuItem.imageUri = Uri.EMPTY;
        menuItem.subItems = savedInstanceState.getIntegerArrayList("subitems");
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
                    Log.d("MADAPP", "imageProfileUri on ActivityResult: " + imageProfileUri.toString());
                    if (imageProfileUri != null)
                        Log.d("MADAPP", "i am here");
                    imageProfileUri = saveImage(imageProfileUri, NewMenuItemActivity.this);
                    imgProfile.setImageURI(imageProfileUri);
                }
                break;
            case GALLERY_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    imageProfileUri = data.getData();
                    if (imageProfileUri != null) {
                        imageProfileUri = saveImage(imageProfileUri, NewMenuItemActivity.this);
                        imgProfile.setImageURI(imageProfileUri);
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
        outState.putString("time", time.getText().toString());
        outState.putString("availability", availability.getText().toString());
        outState.putString("category", category.getText().toString());

        if (imageProfileUri != Uri.EMPTY)
            outState.putString("imageUri", imageProfileUri.toString());
        else
            outState.putString("imageUri", "");

    }

    private void updateFields(MenuItemRest u) {
        Log.d("Update", u.name);
        name.setText(u.name);
        description.setText(u.description);
        time.setText(u.ttl.toString());
        price.setText(u.price.toString());
        availability.setText(u.availability.toString());
        loadSubItems(u.subItems);

        category.setText(u.category);

        Log.d("TAG", u.category);

        if (u.imageUri == Uri.EMPTY || u.imageUri.toString().equals("")) {
            Log.d("MADAPP", "Setting user default image");
            imageProfileUri = Uri.EMPTY;

            imgProfile.setImageDrawable(getDrawable(R.drawable.dish_icon));
        } else {
            Log.d("MADAPP", "Setting custom user image");
            imageProfileUri = u.imageUri;
            imgProfile.setImageURI(u.imageUri);
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
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Log.d("PHOTO", "scvcatto foto");
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
                            Log.d("PHOTO", " non null");

                            imageProfileUri = FileProvider.getUriForFile(NewMenuItemActivity.this,
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);
                            Log.d("PHOTO", " CONTINUO");

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

                    if (index == -1) {
                        Database.getInstance().addMenuItems(name.getText().toString(), description.getText().toString(), category.getText().toString(), price.getText().toString(), availability.getText().toString(), time.getText().toString(), imageProfileUri.toString(), imageProfileUri, subItems);
                    } else {
                        Database.getInstance().setMenuItems(index, name.getText().toString(), category.getText().toString(), description.getText().toString(), price.getText().toString(), availability.getText().toString(), time.getText().toString(), imageProfileUri.toString(), imageProfileUri, subItems);
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
