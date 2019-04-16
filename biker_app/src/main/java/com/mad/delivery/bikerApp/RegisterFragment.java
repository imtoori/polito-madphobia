package com.mad.delivery.bikerApp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private View view;
    private Button btnRegister, btnLogin;
    private EditText email, password, passwordConfirm;
    private FirebaseAuth mAuth;

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
                registerUser(email.getText().toString(), password.getText().toString(), passwordConfirm.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void registerUser(String emailAddress, String pwd, String pwdc) {
        if(emailAddress == null || pwd == null || pwdc == null) return;
        if(emailAddress.equals("") || pwd.equals("") || pwdc.equals("")) return;
        if(!pwd.equals(pwdc)) return;

        mAuth.createUserWithEmailAndPassword(emailAddress, pwd)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MADAPP", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MADAPP", "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

}
