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
    private WalletFragment.OnOrderSelected mListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CardView cvNoLogin, cvCredit;
    private Button btnLogin, btnUse;
    private OrdersAdapter ordersAdapter;
    private List<Order> orders;
    private RecyclerView recyclerView;
    private TextView totalCredit, orderTitleTv;
    private EditText creditCode;
    private View separator;
    private ProgressBar pgBar;
    private TextView noOrder;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        setHasOptionsMenu(true);
        noOrder = v.findViewById(R.id.tv_no_orders);
        currentUser = mAuth.getCurrentUser();
        pgBar = v.findViewById(R.id.pg_bar);
        recyclerView = v.findViewById(R.id.orders_rv);
        orders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orders, mListener);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ordersAdapter);

        cvNoLogin = v.findViewById(R.id.no_login_cv);
        orderTitleTv = v.findViewById(R.id.textView4);
        separator=v.findViewById(R.id.separator);
        cvCredit = v.findViewById(R.id.cv_credit);
        totalCredit = v.findViewById(R.id.total_credit);
        btnLogin = v.findViewById(R.id.btn_login);
        creditCode = v.findViewById(R.id.credit_code);
        cvNoLogin = v.findViewById(R.id.no_login_cv);
        btnUse = v.findViewById(R.id.done_code);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("MADAPP", "WalletFragment onViewCreated");
        if (currentUser == null) {
            cvNoLogin.setVisibility(View.VISIBLE);
        } else {
            ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
                @Override
                public void onSuccess(User user) {
                    cvCredit.setVisibility(View.VISIBLE);
                    orderTitleTv.setVisibility(View.VISIBLE);
                    separator.setVisibility(View.VISIBLE);
                    checkCredit();
                    ConsumerDatabase.getInstance().getAllCostumerOrders(user.id, ords -> {
                        if(ords == null || ords.size() == 0) {
                            noOrder.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            orders.clear();
                            ordersAdapter.notifyDataSetChanged();
                            orders.addAll(ords);
                            ordersAdapter.notifyDataSetChanged();
                            noOrder.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        pgBar.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onFailure() {

                    cvNoLogin.setVisibility(View.VISIBLE);
                    pgBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumerDatabase.getInstance().checkCoupon(creditCode.getText().toString(), item -> {
                    if(item == null) {
                        creditCode.setError("Your coupon is not valid.");
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
    public void onResume() {
        super.onResume();
        Log.d("MADAPP", "WalletFragment onResume");
    }

    public interface OnOrderSelected {
        void openOrder(Order o);
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
