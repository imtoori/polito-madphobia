package com.mad.delivery.consumerApp.settings;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CardView cvNoLogin, cvProfile, cvPrivacy, cvLogout;
    private LinearLayout linearLayoutLogin;
    private Button btnLogin;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        btnLogin = view.findViewById(R.id.btn_login);
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            cvNoLogin = view.findViewById(R.id.no_login_cv);
            cvNoLogin.setVisibility(View.VISIBLE);

        } else {
            linearLayoutLogin = view.findViewById(R.id.profile_ll);
            linearLayoutLogin.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cvProfile = view.findViewById(R.id.cv_profile);
        //cvHour = view.findViewById(R.id.cv_hours);
        cvPrivacy = view.findViewById(R.id.cv_privacy);
        //cvLanguage = view.findViewById(R.id.cv_language);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cvLogout = view.findViewById(R.id.cv_logout);
        cvLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            ConsumerDatabase.getInstance().reset();
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        return view;
    }

}
