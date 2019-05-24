package com.mad.delivery.consumerApp.wallet;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.firebaseCallback;
import com.mad.delivery.consumerApp.search.CategoriesFragment;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    private WalletFragment.OnOrderSelected mListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CardView cvNoLogin, cvCredit;
    private Button btnLogin;


    RecyclerView recyclerView;
    TextView totalCredit, textView;
    EditText Creditcode;

    public WalletFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        setHasOptionsMenu(true);

        currentUser = mAuth.getCurrentUser();
        recyclerView = v.findViewById(R.id.orders_rv);
        textView = v.findViewById(R.id.textView4);
        cvCredit = v.findViewById(R.id.cv_credit);
        totalCredit = v.findViewById(R.id.total_credit);
        btnLogin = v.findViewById(R.id.btn_login);
        if (currentUser == null) {
            cvNoLogin = v.findViewById(R.id.no_login_cv);
            cvNoLogin.setVisibility(View.VISIBLE);

        } else {

            ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
                @Override
                public void onSuccess(User user) {
                    recyclerView.setVisibility(View.VISIBLE);
                    cvCredit.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    Creditcode = v.findViewById(R.id.credit_code);


                    checkCredit();
                    List<Order> orders = new ArrayList<>();
                    ConsumerDatabase.getInstance().getAllCostumerOrders(new firebaseCallback<List<Order>>() {
                        @Override
                        public void onCallBack(List<Order> item) {
                            OrdersAdapter ordersAdapter = new OrdersAdapter(item, mListener);
                            recyclerView.hasFixedSize();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(ordersAdapter);
                        }
                    });
                }

                @Override
                public void onFailure() {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });




        }
        v.findViewById(R.id.done_code).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConsumerDatabase.getInstance().checkCreditCode(Creditcode.getText().toString(), new firebaseCallback<CreditCode>() {
                    @Override
                    public void onCallBack(CreditCode item) {
                        Double val = item.value;
                        Log.i("MADD","val->"+val);
                        ConsumerDatabase.getInstance().updateCreditCustomer(item.value, new firebaseCallback<Boolean>() {
                            @Override
                            public void onCallBack(Boolean item) {
                                if (item) {
                                    Log.d("MADD", "Il tuo conto è stato aumentato di " + val);
                                    checkCredit();
                                    Toast.makeText(getContext(), getString(R.string.IncreasedCredit) + " " + val, Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("MADD", "ti devi registre");
                                    Toast.makeText(getContext(), getString(R.string.ErrorCredit), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
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

        return v;


    }

    public interface OnOrderSelected {
        void openOrder();
    }

    public void checkCredit() {
        ConsumerDatabase.getInstance().getUserId(new firebaseCallback<User>() {
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
