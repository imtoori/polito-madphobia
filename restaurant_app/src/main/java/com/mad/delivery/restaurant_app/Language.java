package com.mad.delivery.restaurant_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Language extends AppCompatActivity {

    Menu menu;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_language);

        myToolbar = (Toolbar) findViewById(R.id.inputlang_toolbar);
        setTitle(getResources().getString(R.string.language_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Language.this);
        switch (item.getItemId()) {
            case R.id.language_it:
                findViewById(R.id.language_it).setBackgroundColor(0xffff0000);
                Log.i("MAD", "ITALIANO");
                /*alertDialog.setTitle("Change Language in Italiano?");
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
                /*alertDialog.setTitle("Change Language in English?");
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