package com.mad.delivery.consumerApp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Menu menu;
    TextInputEditText name;
    TextInputEditText emailAddress;
    TextInputEditText description;
    TextInputEditText deliveryAddress;
    ImageView imgProfile;
    String imageProfileString;
    final int galleryCode = 1;
    final int cameraCode = 2;
    final int READ_EXTERNAL_STORAGE_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        getProfileData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_option:
                enableProfileData(true);
                item.setVisible(false);
                MenuItem itemDone = menu.findItem(R.id.edit_profile_done);
                itemDone.setVisible(true);

                return true;
            case R.id.edit_profile_done:
                enableProfileData(false);
                MenuItem itemEdit = menu.findItem(R.id.edit_profile_option);
                itemEdit.setVisible(true); // edit button is visible again
                item.setVisible(false); // done button is hidden
                setProfileData();
                Toast.makeText(MainActivity.this, "Your profile has saved", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageProfileString));
                } else {
                    // permission denied
                    imgProfile.setImageDrawable(getDrawable(R.drawable.user_avatar));
                }
                return;
            }
            default:
                // do nothing
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case cameraCode:
                if (resultCode == RESULT_OK && data != null) {
                    imgProfile.setImageBitmap((Bitmap) data.getExtras().get("data"));
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
                            imageProfileString = cursor.getString(columnIndex);

                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION
                                );
                            } else
                                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageProfileString));
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

    public void getProfileData() {
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        name.setText(sharedPref.getString("name", ""));
        emailAddress.setText(sharedPref.getString("emailAddress", ""));
        description.setText(sharedPref.getString("description", ""));
        deliveryAddress.setText(sharedPref.getString("deliveryAddress", ""));
        String encodedImage = sharedPref.getString("imageSrc", "");
        if (encodedImage.equals("") || encodedImage == null) {
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_avatar));
        } else {
            imgProfile.setImageBitmap(decodeToBase64(encodedImage));
        }
    }

    public void setProfileData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("emailAddress", emailAddress.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.putString("deliveryAddress", deliveryAddress.getText().toString());
        Bitmap imageBitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
        String encodedImage;
        if (imageBitmap == null) encodedImage = "";
        else encodedImage = encodeToBase64(((BitmapDrawable) imgProfile.getDrawable()).getBitmap());
        editor.putString("imageSrc", encodedImage);
        editor.commit();
    }

    public void enableProfileData(boolean editMode) {
        name.setEnabled(editMode);
        emailAddress.setEnabled(editMode);
        description.setEnabled(editMode);
        deliveryAddress.setEnabled(editMode);
        imgProfile.setClickable(editMode);
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void zoomImage(View view) {
    }
}
