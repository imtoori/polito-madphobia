package com.mad.delivery.bikerApp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;



public class SettingFragment extends Fragment {

    private CardView cvProfile, cvPrivacy, cvLanguage;

    public SettingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        cvProfile = view.findViewById(R.id.cv_profile);
        cvPrivacy = view.findViewById(R.id.cv_privacy);
        //cvLanguage = view.findViewById(R.id.cv_language);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                Log.d("MAD", "profilo");
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
       /* cvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LanguageDialog newFragment = new LanguageDialog();

                newFragment.show(getActivity().getSupportFragmentManager(), "listStatus");

            }
        });*/
        return view;
    }




}
