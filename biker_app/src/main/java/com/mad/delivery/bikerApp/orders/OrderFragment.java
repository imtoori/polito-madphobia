package com.mad.delivery.bikerApp.orders;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.auth.OnLogin;
import com.mad.delivery.bikerApp.settings.GeneralSettingActivity;
import com.mad.delivery.resources.Biker;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    public static final String ORDER_FRAGMENT_TAG = "order_fragment";
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private CardView cvChangeVisibility;
    private Button btnChangeVisibility;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public OrderFragment() {
        // Required empty public constructor
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflator = inflater.inflate(R.layout.fragment_order, container, false);
        mAuth = FirebaseAuth.getInstance();
        cvChangeVisibility = inflator.findViewById(R.id.cv_not_visible);

        btnChangeVisibility = inflator.findViewById(R.id.btn_change_visible);
        btnChangeVisibility.setOnClickListener( view -> {
            Intent intent = new Intent(getContext(), GeneralSettingActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        return inflator;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   setHasOptionsMenu(false);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = view.findViewById(R.id.orders_pager);
        pagerAdapter = new OrdersTabsPageAdapter(getChildFragmentManager(), getContext());
        mPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.tab_header);
        tabLayout.setupWithViewPager(mPager);

        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

        }
        else {
            BikerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Biker>() {
                @Override
                public void onSuccess(Biker user) {
                    // do nothing
                    if(user.visible) cvChangeVisibility.setVisibility(View.GONE);
                    else cvChangeVisibility.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure() {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


}
