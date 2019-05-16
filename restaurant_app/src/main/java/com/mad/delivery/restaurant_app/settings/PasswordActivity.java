package com.mad.delivery.restaurant_app.settings;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;

public class PasswordActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Menu menu;
    EditText currentP;
    EditText newP;
    EditText repeatnewP;
    Toolbar myToolbar;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
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
        intent.putExtra("open", 2);
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
                    Toast.makeText(this, getResources().getString(R.string.saved_password), Toast.LENGTH_LONG).show();
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
            currentP.setError(getResources().getString(R.string.check_current_password));
            result = false;
        }

        //CHECK CURRENT PASSWORD
            if( newP.getText().toString().length()<6){
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
