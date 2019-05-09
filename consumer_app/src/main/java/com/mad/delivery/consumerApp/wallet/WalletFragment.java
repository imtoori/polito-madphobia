package com.mad.delivery.consumerApp.wallet;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.firebaseCallback;
import com.mad.delivery.consumerApp.search.CategoriesFragment;
import com.mad.delivery.resources.CreditCode;
import com.mad.delivery.resources.Customer;
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
    TextView totalCredit;


    public WalletFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
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
        // Inflate the layout for this fragment

        // TODO usare metodo checkCreditCode per verificare un codice di credito
       /* ConsumerDatabase.getInstance().checkCreditCode("TO10", new ConsumerDatabase.firebaseCallback<CreditCode>() {
            @Override
            public void onCallBack(CreditCode item) {
            }
        });
        */

        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = v.findViewById(R.id.orders_rv);
        EditText Creditcode=v.findViewById(R.id.credit_code);
        totalCredit= v.findViewById(R.id.total_credit);
git
        checkCredit();


        List<Order> orders= new ArrayList<>();
        Log.d("MAD","Sono nel wallet!");

        ConsumerDatabase.getInstance().getAllCostumerOrders(new firebaseCallback<List<Order>>() {
            @Override
            public void onCallBack(List<Order> item) {
                OrdersAdapter ordersAdapter = new OrdersAdapter(item, mListener);
                recyclerView.hasFixedSize();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(ordersAdapter);
            }
        });


        v.findViewById(R.id.done_code).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ConsumerDatabase.getInstance().checkCreditCode( "TO10", new firebaseCallback<CreditCode>() {
                    //TODO rimuovere codice TO10 dal codice
                    @Override
                    public void onCallBack(CreditCode item) {
                        Double val = item.value;
                         ConsumerDatabase.getInstance().updateCreditCustomer(item.value, new firebaseCallback<Boolean>() {
                             @Override
                             public void onCallBack(Boolean item) {
                                 if(item){
                                     Log.d("MADD","Il tuo conto è stato aumentato di "+val);
                                     //TODO inserire interrogazione al db su totalCredit
                                     checkCredit();
                                     Toast.makeText(getContext(), getString(R.string.IncreasedCredit)+" "+val, Toast.LENGTH_LONG).show();
                                 }
                                 else {
                                     Log.d("MADD", "ti devi registre");
                                     Toast.makeText(getContext(), getString(R.string.ErrorCredit), Toast.LENGTH_LONG).show();
                                 }
                             }
                         });

                    }
                });
            }
        });
        return v;


    }

    public interface OnOrderSelected {
        void openOrder();
    }

    public void checkCredit(){
        ConsumerDatabase.getInstance().getUserId(new firebaseCallback<User>(){
            @Override
            public void onCallBack(User user) {
                if(user!=null)
                    totalCredit.setText(user.credit.toString());
                else
                    totalCredit.setText(R.string.null_value);


            }
        });
    }

}
