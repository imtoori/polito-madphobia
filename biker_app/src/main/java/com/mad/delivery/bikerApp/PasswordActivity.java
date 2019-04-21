package com.mad.delivery.bikerApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PasswordActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Menu menu;
    EditText currentP;
    EditText newP;
    EditText repeatnewP;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_password);
        myToolbar = (Toolbar) findViewById(R.id.inputpssw_toolbar);
        setTitle(getResources().getString(R.string.password_toolbar));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        currentP = findViewById(R.id.old_pssw);
        newP = findViewById(R.id.new_pssw);
        repeatnewP = findViewById(R.id.new_pssw1);
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
        switch (item.getItemId()) {
            case R.id.edit_profile_done:
                if(checkConstraints()) {
                    setPassword();
                    sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
                    String p=sharedPref.getString("password", "");
                    Toast.makeText(this, "Your password: "+p, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setPassword() {
        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String p= newP.getText().toString();
        editor.putString("password", p);
        editor.commit();
    }



        public boolean checkConstraints(){
        Pattern pattern;
        Matcher matcher;
        boolean result = true;

        final String psswString = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        //deve contenere 1 lettera minuscola, una maiuscola e un numero. min 8 caratteri

        sharedPref = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        String p=sharedPref.getString("password", "");
        if(!currentP.getText().toString().equals(p)){
            currentP.setError(getResources().getString(R.string.check_current_password)+" :"+p+".");
            result = false;
        }

        //CHECK CURRENT PASSWORD
            pattern = Pattern.compile(psswString);
            matcher = pattern.matcher(newP.getText().toString());

            if(!matcher.matches()){
            newP.setError(getResources().getString(R.string.check_password));
            result = false;
        }

        if(!newP.getText().toString().equals(repeatnewP.getText().toString())) {
            repeatnewP.setError(getResources().getString(R.string.check_password_repeat)+":"+newP.getText().toString());
            result = false;
        }

        return result;
    }
}
