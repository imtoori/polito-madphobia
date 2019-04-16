package com.mad.delivery.restaurant_app;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

public class EditProfileActivity extends AppCompatActivity implements TimePickerFragmentOpeningH.TimePickedOpeningHListener {
    SharedPreferences sharedPref;
    Menu menu;
    User mUser;
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
    ImageView imgProfile;
    Toolbar myToolbar;
    Uri imageProfileUri;
    EditText[] day=new EditText[7];
    String currentPhotoPath;
    ImageView[] day_edit=new ImageView[7];
    final int GALLERY_CODE = 1;
    final int CAMERA_CODE = 2;
    boolean[] flag=new boolean[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        imageProfileUri = Uri.EMPTY;
        myToolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setTitle(getResources().getString(R.string.editprofile_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        day[0] = findViewById(R.id.editprofile_day1_input);
        day_edit[0] = findViewById(R.id.editprofile_day1_edit);
        flag[0] = false;
        day[1] = findViewById(R.id.editprofile_day2_input);
        day_edit[1] = findViewById(R.id.editprofile_day2_edit);
        flag[1] = false;
        day[2] = findViewById(R.id.editprofile_day3_input);
        day_edit[2] = findViewById(R.id.editprofile_day3_edit);
        flag[2] = false;
        day[3]= findViewById(R.id.editprofile_day4_input);
        day_edit[3] = findViewById(R.id.editprofile_day4_edit);
        flag[3] = false;
        day[4] = findViewById(R.id.editprofile_day5_input);
        day_edit[4] = findViewById(R.id.editprofile_day5_edit);
        flag[4] = false;
        day[5] = findViewById(R.id.editprofile_day6_input);
        day_edit[5] = findViewById(R.id.editprofile_day6_edit);
        flag[5] = false;
        day[6]= findViewById(R.id.editprofile_day7_input);
        day_edit[6] = findViewById(R.id.editprofile_day7_edit);
        flag[6] = false;
        btnCamera = findViewById(R.id.editprofile_btncamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(EditProfileActivity.this);
            }
        });
        day_edit[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 0);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 1);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 2);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 3);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 4);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 5);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        day_edit[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragmentOpeningH();
                Bundle bundle = new Bundle();
                DateTime d = new DateTime();
                bundle.putSerializable("datetime", d);
                bundle.putInt("day", 6);
                timeFragment.setArguments(bundle);
                timeFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            getProfileData();
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
        outState.putString("day1", day[0].getText().toString());
        outState.putString("day2", day[1].getText().toString());
        outState.putString("day3", day[2].getText().toString());
        outState.putString("day4", day[3].getText().toString());
        outState.putString("day5", day[4].getText().toString());
        outState.putString("day6", day[5].getText().toString());
        outState.putString("day7", day[6].getText().toString());
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
        mUser = new User();
        Log.d("MADAPP", "SavedInstanceState contains data");
        mUser.name = savedInstanceState.getString("name");
        mUser.phoneNumber = savedInstanceState.getString("phoneNumber");
        mUser.email = savedInstanceState.getString("emailAddress");
        mUser.description = savedInstanceState.getString("description");
        mUser.road = savedInstanceState.getString("road");
        mUser.houseNumber = savedInstanceState.getString("houseNumber");
        mUser.doorPhone = savedInstanceState.getString("doorPhone");
        mUser.postCode = savedInstanceState.getString("postCode");
        mUser.city = savedInstanceState.getString("city");
        mUser.imageUri = Uri.parse(savedInstanceState.getString("imageUri"));
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
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        mUser = new User(sharedPref.getString("name", ""),
                sharedPref.getString("phoneNumber", ""),
                sharedPref.getString("emailAddress", ""),
                sharedPref.getString("description", ""),
                sharedPref.getString("opening", ""),
                sharedPref.getString("road", ""),
                sharedPref.getString("houseNumber", ""),
                sharedPref.getString("doorPhone", ""),
                sharedPref.getString("postCode", ""),
                sharedPref.getString("city", ""),
                Uri.parse(sharedPref.getString("imageUri", Uri.EMPTY.toString()))
        );
        day[0].setText(sharedPref.getString("day1", ""));
        day[1].setText(sharedPref.getString("day2", ""));
        day[2].setText(sharedPref.getString("day3", ""));
        day[3].setText(sharedPref.getString("day4", ""));
        day[4].setText(sharedPref.getString("day5", ""));
        day[5].setText(sharedPref.getString("day6", ""));
        day[6].setText(sharedPref.getString("day7", ""));
        Log.d("MADAPP", "GET Profile data = " + Uri.parse(sharedPref.getString("imageUri", Uri.EMPTY.toString())));
        updateFields(mUser);
    }

    private void setProfileData() {
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("phoneNumber", phoneNumber.getText().toString());
        editor.putString("emailAddress", emailAddress.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.putString("road", road.getText().toString());
        editor.putString("houseNumber", houseNumber.getText().toString());
        editor.putString("doorPhone", doorPhone.getText().toString());
        editor.putString("postCode", postCode.getText().toString());
        editor.putString("city", city.getText().toString());
        editor.putString("day1", day[0].getText().toString());
        editor.putString("day2", day[1].getText().toString());
        editor.putString("day3", day[2].getText().toString());
        editor.putString("day4", day[3].getText().toString());
        editor.putString("day5", day[4].getText().toString());
        editor.putString("day6", day[5].getText().toString());
        editor.putString("day7", day[6].getText().toString());
        try {
            Log.d("MADAPP", "ImageProfileURI=" + imageProfileUri.toString());
            if (imageProfileUri != Uri.EMPTY) {
                editor.putString("imageUri", imageProfileUri.toString());
            } else {
                Log.d("MADAPP", "added empty");
                editor.putString("imageUri", Uri.EMPTY.toString());
            }

        } catch (NullPointerException e) {
            editor.putString("imageUri", Uri.EMPTY.toString());
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
        road.setText(u.road);
        houseNumber.setText(u.houseNumber);
        doorPhone.setText(u.doorPhone);
        postCode.setText(String.valueOf(u.postCode));
        city.setText(u.city);

        if (u.imageUri == Uri.EMPTY || u.imageUri.toString().equals("")) {
            Log.d("MADAPP", "Setting user default image");
            imageProfileUri = Uri.EMPTY;
            imgProfile.setImageDrawable(getDrawable(R.drawable.user_default));
        } else {
            Log.d("MADAPP", "Setting custom user image");
            imageProfileUri = u.imageUri;
            imgProfile.setImageURI(u.imageUri);
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
        Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9].*");

        Matcher m;
        for(int i=0; i<7; i++) {
            m = p.matcher(day[i].getText().toString());
            if (!day[i].getText().toString().equals("") && (!m.matches() || checkhours(day[i].getText().toString()))) {
                day[i].setError(getResources().getString(R.string.check_time));
                result = false;
            }
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

    private boolean checkhours(String time) {
        String[] s = time.split("-");
        String[] start = s[0].split(":");
        String[] end = s[1].split(":");
        if (start[0].compareTo(end[0]) < 0)
            return false;
        if (start[0].compareTo(end[0]) == 0)
            if (start[1].compareTo(end[1]) <= 0)
                return false;
        return true;
    }


    @Override
    public void onTimePicked(int h, int m, int i) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        DateTime d = new DateTime();
        d = d.hourOfDay().setCopy(h);
        d = d.minuteOfHour().setCopy(m);
        if (flag[i] == false) {
            day[i].setText(d.toString(dtf));
            flag[i] = true;
            DialogFragment timeFragment = new TimePickerFragmentOpeningH();
            Bundle bundle = new Bundle();
            DateTime t = new DateTime();
            bundle.putSerializable("datetime", t);
            bundle.putInt("day",i);
            timeFragment.setArguments(bundle);
            timeFragment.show(getSupportFragmentManager(), "timePicker");
        } else {
            day[i].append("-" + d.toString(dtf));
            flag[i] = false;
        }
    }

}