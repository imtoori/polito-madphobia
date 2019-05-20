package com.mad.delivery.bikerApp.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
<<<<<<< HEAD
import com.mad.delivery.bikerApp.BikerDatabase;
=======
>>>>>>> a87719c5b486b5f44530eaa02e0877d1d465bca7
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.auth.OnLogin;
import com.mad.delivery.resources.Biker;

public class PasswordActivity extends AppCompatActivity {
    Menu menu;
    EditText currentP;
    EditText newP;
    EditText repeatnewP;
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

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
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        BikerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Biker>() {
            @Override
            public void onSuccess(Biker user) {
                // do nothing
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("open", 2);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
                String email = currentUser.getEmail();
                String oldPassword = currentP.getText().toString();
                String newPassword = newP.getText().toString();
                String repeatPassword = repeatnewP.getText().toString();

                if (oldPassword.length() == 0) {
                    currentP.setError("Your current password is empty.");
                    return true;
                }

                // check if newP and repeatnewP are equals
                if (!newPassword.equals(repeatPassword)) {
                    repeatnewP.setError("The two passwords don't match.");
                    return true;
                }
                if (newPassword.length() < 6) {
                    newP.setError("Your password must be length atleast 6 characters.");
                    return true;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener(innerTask -> {
                            if (!innerTask.isSuccessful()) {
                                //something goes worng
                                currentP.setError("Your current password is wrong");
                            } else {
                                Toast.makeText(PasswordActivity.this, "Your password has been modified", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });
                    } else {
                        currentP.setError("Your current password is wrong");
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}