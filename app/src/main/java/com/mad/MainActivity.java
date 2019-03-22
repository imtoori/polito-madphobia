package com.mad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    TextInputEditText name;
    TextInputEditText emailAddress;
    TextInputEditText description;
    TextInputEditText deliveryAddress;
    ImageView imgProfile;
    boolean editMode = false;
    final int galleryCode = 1;
    final int cameraCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        name = (TextInputEditText) findViewById(R.id.name);
        emailAddress = (TextInputEditText) findViewById(R.id.emailAddress);
        description = (TextInputEditText) findViewById(R.id.description);
        deliveryAddress = (TextInputEditText) findViewById(R.id.deliveryAddress);
        imgProfile = (ImageView) findViewById(R.id.image_profile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(MainActivity.this);
            }
        });
        imgProfile.setClickable(false);

        name.setText(sharedPref.getString("name", ""));
        emailAddress.setText(sharedPref.getString("emailAddress", ""));
        description.setText(sharedPref.getString("description", ""));
        deliveryAddress.setText(sharedPref.getString("deliveryAddress", ""));
        String imageURIString = sharedPref.getString("imageSrc", null);
        if (imageURIString == null)
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_avatar));
        else {
            Bitmap bitmapImage = StringToBitMap(imageURIString);
            imgProfile.setImageBitmap(bitmapImage);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_option:
                editMode = !editMode;
                name.setEnabled(editMode);
                emailAddress.setEnabled(editMode);
                description.setEnabled(editMode);
                deliveryAddress.setEnabled(editMode);
                imgProfile.setClickable(editMode);
                if (editMode) item.setIcon(R.drawable.ic_done);
                else item.setIcon(R.drawable.ic_edit_profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case cameraCode:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                    imgProfile.setImageBitmap(selectedImage);
                }

                break;
            case galleryCode:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                            cursor.close();
                        }
                    }

                }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
                    startActivityForResult(takePicture, cameraCode);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, galleryCode);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void saveUserProfile() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("emailAddress", emailAddress.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.putString("deliveryAddress", deliveryAddress.getText().toString());
        String imageBitMap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap().toString();
        editor.putString("imageSrc", imageBitMap);
        editor.commit();
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
