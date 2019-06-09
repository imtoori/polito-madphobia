package com.mad.delivery.restaurant_app.auth;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.mad.delivery.restaurant_app.R;


public class LoginActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private RelativeLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgLogo = findViewById(R.id.img_register_logo);
        container = findViewById(R.id.layout_container);

        ObjectAnimator animation = ObjectAnimator.ofFloat(imgLogo, "translationY", -650f).setDuration(1000);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(container, "translationY", -650f).setDuration(1000);
        animation.start();
        animation2.start();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment login = new LoginFragment();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.layout_container, login, "loginFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
