package com.mad.delivery.bikerApp.auth;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Restaurant;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView email, password, error;
    private Button loginBtn, registerBtn;
    private View progressView;
    private Animation shakeAnim;
    private Biker biker;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        progressView = view.findViewById(R.id.pg_login);
        mAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.tv_login_email);
        password = view.findViewById(R.id.tv_login_password);
        loginBtn = view.findViewById(R.id.btn_login);
        registerBtn = view.findViewById(R.id.btn_register);
        error = view.findViewById(R.id.errorView);
        shakeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_effect2);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(email, password);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment register = new RegisterFragment();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fragmentTransaction.replace(R.id.layout_container, register, "registerFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
        return view;
    }


    private void signInUser(final TextView email, final TextView password) {
        progressView.setVisibility(View.VISIBLE);
        Log.d("MADAPP", "visible");
        boolean error = false;
        if (email.getText() == null || email.getText().toString().equals("")) {
            email.startAnimation(shakeAnim);
            email.setError(getResources().getString(R.string.login_error_email_empty));
            error = true;
        }
        if (password.getText() == null || password.getText().toString().equals("")) {
            password.startAnimation(shakeAnim);
            password.setError(getResources().getString(R.string.login_error_password_empty));
            error = true;
        }
        if (error) {
            progressView.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressView.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MADAPP", "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            // check if the user is registered on the right app (Restaurant, Customer, Biker)
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            Log.d("MADAPP", mAuth.getUid());
                            DatabaseReference ref = database.getReference("users").child("biker").child(mAuth.getUid());
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Log.d("MADAPP", "datasnapshot exists");
                                        biker = dataSnapshot.getValue(Biker.class);
                                        if (biker == null) {
                                            password.startAnimation(shakeAnim);
                                            email.startAnimation(shakeAnim);
                                            email.setError(getResources().getString(R.string.login_error_fail));
                                            password.setError(getResources().getString(R.string.login_error_fail));
                                            return;
                                        } else {
                                            Log.d("MADAPP", "This user is not a biker!");
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("user", biker);
                                            Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                                            intent.putExtra("user", bundle);
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        }
                                    } else {
                                        Log.d("MADAPP", "Datasnapshots does not exist");
                                        password.startAnimation(shakeAnim);
                                        email.startAnimation(shakeAnim);
                                        email.setError(getResources().getString(R.string.login_error_fail));
                                        password.setError(getResources().getString(R.string.login_error_fail));
                                        biker = null;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    password.startAnimation(shakeAnim);
                                    email.startAnimation(shakeAnim);
                                    email.setError(getResources().getString(R.string.login_error_fail));
                                    password.setError(getResources().getString(R.string.login_error_fail));
                                    biker = null;
                                }
                            });
                        }
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    password.startAnimation(shakeAnim);
                    password.setError(getResources().getString(R.string.register_pwd_too_weak));
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    password.startAnimation(shakeAnim);
                    email.startAnimation(shakeAnim);
                    email.setError(getResources().getString(R.string.login_error_fail));
                    password.setError(getResources().getString(R.string.login_error_fail));
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // The email address is badly formatted.
                    password.startAnimation(shakeAnim);
                    email.startAnimation(shakeAnim);
                    email.setError(getResources().getString(R.string.login_error_fail));
                    password.setError(getResources().getString(R.string.login_error_fail));
                } else {
                    openErrorView(R.string.register_generic_error);
                }

            }
        });
    }

    public void openErrorView(int id) {
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

    public void closeErrorView() {
        error.animate().setDuration(1000).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                error.setVisibility(View.INVISIBLE);
            }
        });
    }
}
