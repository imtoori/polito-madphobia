package com.mad.delivery.bikerApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ImageView imgLogo;
    private RelativeLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgLogo = findViewById(R.id.img_register_logo);
        container = findViewById(R.id.layout_container);

        ObjectAnimator animation = ObjectAnimator.ofFloat(imgLogo, "translationY", -700f).setDuration(1000);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(container, "translationY", -700f).setDuration(1000);
        animation.start();
        animation2.start();

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        LoginFragment login = new LoginFragment();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.layout_container, login,"loginFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }



}
