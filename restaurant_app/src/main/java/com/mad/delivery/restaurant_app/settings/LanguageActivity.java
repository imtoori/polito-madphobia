package com.mad.delivery.restaurant_app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;


public class LanguageActivity extends AppCompatActivity {

    Menu menu;
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_language);
        mAuth = FirebaseAuth.getInstance();
        myToolbar = (Toolbar) findViewById(R.id.inputlang_toolbar);
        setTitle(getResources().getString(R.string.language_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LanguageActivity.this);
        switch (item.getItemId()) {
            case R.id.language_it:
                findViewById(R.id.language_it).setBackgroundColor(0xffff0000);
                Log.i("MAD", "ITALIANO");
                /*alertDialog.setTitle("Change LanguageActivity in Italiano?");
                alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LanguageHelper.changeLocale(getResources(), "it");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
                alertDialog.setNegativeButton("Cancel", null);*/
                break;

            case R.id.language_en:
                findViewById(R.id.language_it).setBackgroundColor(0xffff0000);
                Log.i("MAD", "Inglese");
                /*alertDialog.setTitle("Change LanguageActivity in English?");
                alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LanguageHelper.changeLocale(getResources(), "en");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
                alertDialog.setNegativeButton("Cancel", null);*/
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


}