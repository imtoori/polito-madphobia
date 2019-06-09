package com.mad.delivery.bikerApp.settings;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.MapsActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.auth.OnLogin;
import com.mad.delivery.resources.Biker;


public class SettingFragment extends Fragment {
    public static final String SETTING_FRAGMENT_TAG = "settings_fragment";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private CardView cvProfile, cvPrivacy, cvLogout,cvMaps,cvGeneral;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        BikerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Biker>() {
            @Override
            public void onSuccess(Biker user) {
                // do nothing
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mAuth = FirebaseAuth.getInstance();
        cvProfile = view.findViewById(R.id.cv_profile);
        cvGeneral = view.findViewById(R.id.cv_general);
        cvPrivacy = view.findViewById(R.id.cv_privacy);
        //cvLanguage = view.findViewById(R.id.cv_language);
        cvLogout = view.findViewById(R.id.cv_logout);
       // cvMaps = view.findViewById(R.id.cv_maps);
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
        cvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GeneralSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cvLogout.setOnClickListener(v -> {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.getUid() + ".order.new");
            mAuth.signOut();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

      /*  cvMaps.setOnClickListener(v->{
            Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });*/
        return view;
    }




}
