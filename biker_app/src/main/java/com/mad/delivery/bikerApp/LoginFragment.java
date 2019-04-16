package com.mad.delivery.bikerApp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView email, password;
    private Button loginBtn, registerBtn;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_login, container, false);

        FirebaseApp.initializeApp(getActivity().getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.tv_login_email);
        password = view.findViewById(R.id.tv_login_password);
        loginBtn = view.findViewById(R.id.btn_login);
        registerBtn = view.findViewById(R.id.btn_register);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(email.getText().toString(), password.getText().toString());
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment register = new RegisterFragment();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fragmentTransaction.replace(R.id.layout_container, register,"registerFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void signInUser(String email, String password) {
        if(email == null || password == null || email.equals("") || password.equals("")) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MADAPP", "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user", user);
                            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("user", bundle);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MADAPP", "signInWithEmail:failure", task.getException());

                        }

                    }
                });
    }
}
