package com.mad.delivery.restaurant_app.auth;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.resources.User;

import org.joda.time.DateTime;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private View view;
    private Button btnRegister, btnLogin;
    private EditText email, password, passwordConfirm;
    private FirebaseAuth mAuth;
    private TextView error;
    private View progressView;
    private Animation shakeAnim;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = view.findViewById(R.id.btn_register);
        btnLogin = view.findViewById(R.id.btn_login);
        email = view.findViewById(R.id.et_register_email);
        password = view.findViewById(R.id.et_register_pwd);
        passwordConfirm = view.findViewById(R.id.et_register_pwd_c);
        shakeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_effect2);
        error = view.findViewById(R.id.tv_error);
        progressView = view.findViewById(R.id.pg_register);
        db = FirebaseDatabase.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                LoginFragment login = new LoginFragment();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.layout_container, login,"loginFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(email, password, passwordConfirm);
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    closeErrorView();
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    closeErrorView();
                }
            }
        });
        passwordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    closeErrorView();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void registerUser(final EditText emailAddress, final EditText pwd, final EditText pwdc) {
        boolean inputsNotEmpty = true;
        progressView.setVisibility(View.VISIBLE);
        if(emailAddress.getText() == null || emailAddress.getText().toString().equals("")) {
            emailAddress.startAnimation(shakeAnim);
            emailAddress.setError(getResources().getString(R.string.register_email_empty));
            inputsNotEmpty = false;
        }
        if(pwd.getText() == null || pwd.getText().toString().equals("")) {
            pwd.startAnimation(shakeAnim);
            pwd.setError(getResources().getString(R.string.register_pwd_empty));
            inputsNotEmpty = false;
        }
        if(pwdc.getText() == null || pwdc.getText().toString().equals("")) {
            pwdc.startAnimation(shakeAnim);
            pwdc.setError(getResources().getString(R.string.register_pwdc_empty));
            inputsNotEmpty = false;
        }

        if(!inputsNotEmpty) {
            progressView.setVisibility(View.GONE);
            return;
        }
        if(!pwd.getText().toString().equals(pwdc.getText().toString())) {
            pwd.setError(getResources().getString(R.string.passwords_does_not_match));
            pwdc.setError(getResources().getString(R.string.passwords_does_not_match));
            progressView.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailAddress.getText().toString(), pwd.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressView.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MADAPP", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            myRef = db.getReference();
                            Restaurant registered = new Restaurant();
                            registered.registrationDate = new DateTime().toString();
                            registered.email = emailAddress.getText().toString();
                            
                            myRef.child("users").child("restaurants").child(user.getUid()).setValue(registered);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user", registered);
                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            intent.putExtra("user", bundle);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MADAPP", "createUserWithEmail:failure", task.getException());

                            if(task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                pwd.startAnimation(shakeAnim);
                                pwdc.startAnimation(shakeAnim);
                                pwd.setError(getResources().getString(R.string.register_pwd_too_weak));
                                pwdc.setError(getResources().getString(R.string.register_pwd_too_weak));
                            }  else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                emailAddress.startAnimation(shakeAnim);
                                emailAddress.setError(getResources().getString(R.string.register_email_already_used));
                            }  else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The email address is badly formatted.
                                emailAddress.startAnimation(shakeAnim);
                                emailAddress.setError(getResources().getString(R.string.register_email_bad_formatted));
                            } else {
                                openErrorView(R.string.register_generic_error);
                            }

                        }

                        // ...
                    }
                });
    }

    public void openErrorView ( int id){
        error.setText(getResources().getString(id));
        error.animate().setDuration(1000).alpha(255).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                error.setVisibility(View.VISIBLE);
            }
        });
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_effect2);
            error.startAnimation(anim);
    }
    public void closeErrorView () {
        Log.d("MADAPP", "called closeErrorView");
        error.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                error.setVisibility(View.INVISIBLE);
            }
        });
    }

}
