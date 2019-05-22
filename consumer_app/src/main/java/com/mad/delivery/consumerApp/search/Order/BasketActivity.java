package com.mad.delivery.consumerApp.search.Order;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.TimePickerFragment;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.firebaseCallback;
import com.mad.delivery.consumerApp.wallet.OrdersAdapter;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BasketActivity extends AppCompatActivity implements OnProductListener  {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private FirebaseUser currentUser;
    private ProductsAdapter myOrderAdapter;
    private RecyclerView rvOrder;
    private User user;
    private List<Product> myOrder;
    private Restaurant restaurant;
    TextView deliveryFee, minOrder, totalCost, minOrderTitle;
    AutoCompleteTextView where, when;
    Chip cash, credit, checkedChip;
    EditText notes;
    Button btnCompleteOrder;
    ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_basket);
        toolbar = findViewById(R.id.myToolbar);
        setTitle(getResources().getString(R.string.Basket_toolbar));
        setSupportActionBar(toolbar);
        chipGroup = findViewById(R.id.chip_group);
        rvOrder = findViewById(R.id.rv_order);
        deliveryFee = findViewById(R.id.tv_delivery_fee_value);
        minOrder = findViewById(R.id.tv_minorder_value);
        minOrderTitle = findViewById(R.id.tv_minorder);
        totalCost = findViewById(R.id.tv_totalcost_value);
        where = findViewById(R.id.actv_delivery_address);
        when = findViewById(R.id.actv_time_delivery);
        cash = findViewById(R.id.chip_cash);
        credit = findViewById(R.id.chip_credit);
        notes = findViewById(R.id.notes);
        btnCompleteOrder = findViewById(R.id.button_complete_order);

        try {
            myOrder = new ArrayList<>(((Map<String, Product>) getIntent().getExtras().get("myOrder")).values());
        } catch (NullPointerException e) {
            myOrder = new ArrayList<>();
            Log.d("MADAPP", "Error: order null");
        }

        restaurant = getIntent().getParcelableExtra("restaurant");

        myOrderAdapter = new ProductsAdapter(myOrder, this);
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.hasFixedSize();
        rvOrder.setAdapter(myOrderAdapter);
        computeTotalPrice();
        chipGroup.setSingleSelection(true);


        CompoundButton.OnCheckedChangeListener filterChipListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Chip chip = (Chip) buttonView;
                if(isChecked) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    checkedChip = chip;
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                }
            }
        };
        cash.setOnCheckedChangeListener(filterChipListener);
        credit.setOnCheckedChangeListener(filterChipListener);

        btnCompleteOrder.setOnClickListener(v -> {
            boolean valid = checkConstraints();
            if(valid) {
                // todo store myOrder
                Log.d("MADAPP", "Order is being saved");
                Toast.makeText(this, "Order is ready to be saved. But this function need to be implemented.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all fields with valid data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
            @Override
            public void onSuccess(User u) {
                user = u;
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i("MADAPP", "onSupportNavigateUp");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.i("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRemoved(Product p) {
        myOrder.removeIf(item -> item.idItem.equals(p.idItem));
        myOrderAdapter.notifyDataSetChanged();
        computeTotalPrice();
        if(myOrder.size() == 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", 1);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public void computeTotalPrice() {
        deliveryFee.setText("€ "+restaurant.previewInfo.deliveryCost);
        double tp = 0.0;
        tp = myOrder.stream().mapToDouble(i -> i.price).sum() + restaurant.previewInfo.deliveryCost;

        if(restaurant.previewInfo.minOrderCost > tp) {
            double remainder = restaurant.previewInfo.minOrderCost - tp;
            tp += remainder;
            minOrder.setText("+" +remainder);
            minOrder.setVisibility(View.VISIBLE);
            minOrderTitle.setVisibility(View.VISIBLE);
        } else {
            minOrder.setVisibility(View.GONE);
            minOrderTitle.setVisibility(View.GONE);
            minOrder.setText("+0.00");
        }
        totalCost.setText("€ " + tp);
    }

    private boolean checkConstraints() {
        if(myOrder.size() == 0) {
            return false;
        }
        if(where.getText().toString().equals("")) {
            where.setError(getResources().getString(R.string.invalid_address));
            return false;
        }
        if(when.getText().toString().equals("")) {
            when.setError(getResources().getString(R.string.invalid_time));
            return false;
        }
        if(checkedChip == null) {
            return false;
        }
        return true;
    }

}