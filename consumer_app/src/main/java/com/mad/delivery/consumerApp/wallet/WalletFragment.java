package com.mad.delivery.consumerApp.wallet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.FirebaseCallback;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    public WalletFragment.OnOrderSelected mListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CardView cvNoLogin, cvCredit;
    private Button btnLogin, btnUse;
    public OrdersAdapter ordersAdapter;
    public List<Order> orders;
    public RecyclerView recyclerView;
    TextView totalCredit, textView, o_tv;
    EditText Creditcode;
    View separator;
    public ProgressBar pgBar;
    public TextView noOrder;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("MADAPP", "WalletFragment onAttach");
        if (context instanceof WalletFragment.OnOrderSelected) {
            mListener = (WalletFragment.OnOrderSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRestaurantSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("MADAPP", "WalletFragment onDetach");

        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("MADAPP", "WalletFragment onViewCreated");
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumerDatabase.getInstance().checkCoupon(Creditcode.getText().toString(), item -> {
                    if(item == null) {
                        Creditcode.setError("Your coupon is not valid.");
                        return;
                    }

                    ConsumerDatabase.getInstance().updateCreditCustomer(item.value, status -> {
                        if (status) {
                            checkCredit();
                            Toast.makeText(getContext(), getString(R.string.IncreasedCredit) + " " + item.value, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.ErrorCredit), Toast.LENGTH_LONG).show();
                        }
                    });
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MADAPP", "WalletFragment onStart");
        if (currentUser == null) {
            cvNoLogin.setVisibility(View.VISIBLE);
        } else {
            ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
                @Override
                public void onSuccess(User user) {
                    cvCredit.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    o_tv.setVisibility(View.VISIBLE);
                    separator.setVisibility(View.VISIBLE);
                    orders = new ArrayList<>();
                    ordersAdapter = new OrdersAdapter(orders, mListener);
                    recyclerView.setAdapter(ordersAdapter);
                    checkCredit();
                    mListener.loadOrders(user.id);
                }

                @Override
                public void onFailure() {

                    cvNoLogin.setVisibility(View.VISIBLE);
                    pgBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MADAPP", "WalletFragment onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        Log.d("MADAPP", "WalletFragment onCreateView");
        setHasOptionsMenu(true);
        noOrder = v.findViewById(R.id.tv_no_orders);
        currentUser = mAuth.getCurrentUser();
        pgBar = v.findViewById(R.id.pg_bar);
        recyclerView = v.findViewById(R.id.orders_rv);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cvNoLogin = v.findViewById(R.id.no_login_cv);
        o_tv= v.findViewById(R.id.textView4);
        separator=v.findViewById(R.id.separator);

        textView = v.findViewById(R.id.textView4);
        cvCredit = v.findViewById(R.id.cv_credit);
        totalCredit = v.findViewById(R.id.total_credit);
        btnLogin = v.findViewById(R.id.btn_login);
        Creditcode = v.findViewById(R.id.credit_code);
        cvNoLogin = v.findViewById(R.id.no_login_cv);
        btnUse = v.findViewById(R.id.done_code);
        return v;
    }

    public interface OnOrderSelected {
        void openOrder(Order o);
        void openFeedbackDialog(Order o);
        void loadOrders(String id);
    }

    public void checkCredit() {
        ConsumerDatabase.getInstance().getUserId(new FirebaseCallback<User>() {
            @Override
            public void onCallBack(User user) {
                if (user != null && user.credit!=null)
                    totalCredit.setText(user.credit.toString());
                else
                    totalCredit.setText(R.string.null_value);


            }
        });
    }

}
