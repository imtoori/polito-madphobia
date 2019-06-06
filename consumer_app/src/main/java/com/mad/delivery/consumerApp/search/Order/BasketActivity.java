package com.mad.delivery.consumerApp.search.Order;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.GPSTracker;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class BasketActivity extends AppCompatActivity implements OnProductListener {
    private FirebaseAuth mAuth;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private FirebaseUser currentUser;
    private ProductsAdapter myOrderAdapter;
    private RecyclerView rvOrder;
    private User user;
    private List<Product> myOrder;
    private Restaurant restaurant;
    private ImageView deliveryAddressButton;
    TextView deliveryFee, minOrder, totalCost, minOrderTitle;
    AutoCompleteTextView where;
    TextView when;
    Chip cash, credit, checkedChip;
    EditText notes;
    Button btnCompleteOrder;
    ChipGroup chipGroup;
    private DateTime orderFor;
    CompletingOrderDialogFragment sendingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_basket);
        sharedPref = getSharedPreferences("basket", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        toolbar = findViewById(R.id.myToolbar);
        deliveryAddressButton = findViewById(R.id.imageView_get_location);
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

        when.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(BasketActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    when.setText(selectedHour + ":" + selectedMinute);
                    orderFor = new DateTime();
                    orderFor = orderFor.hourOfDay().setCopy(selectedHour);
                    orderFor = orderFor.minuteOfHour().setCopy(selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Delivery Time");
            mTimePicker.show();
        });

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
                if (isChecked) {
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
            if (valid) {

                String paymentMethod = "";
                if (checkedChip.getText().toString().equals(getString(R.string.pay_cash)))
                    paymentMethod = "cash";
                else if (checkedChip.getText().toString().equals(getString(R.string.pay_credit)))
                    paymentMethod = "credit";
                else paymentMethod = "cash";

                Order order = new Order(user, restaurant, myOrder, orderFor.toString(), paymentMethod, where.getText().toString());
                order.clientNotes = notes.getText().toString();
                sendingFragment = CompletingOrderDialogFragment.newInstance(order);
                sendingFragment.show(getSupportFragmentManager(), "sendingFragment");
            } else {
                Toast.makeText(this, "Please fill all fields with valid data", Toast.LENGTH_SHORT).show();
            }
        });

        deliveryAddressButton.setOnClickListener(v -> {
            GPSTracker gps = new GPSTracker(BasketActivity.this);
            if (gps.isGPSEnabled) {
                Geocoder geocoder;
                List<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(BasketActivity.this, Locale.getDefault());

                try {
                    Double latitude = gps.getLatitude();
                    Double longitude = gps.getLongitude();
                    Log.d("latitude: ", latitude.toString());
                    Log.d("longitude: ", longitude.toString());

                    addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    Log.d("ADDRESS:", address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
                    where.setText(address);
                } else {
                    Toast.makeText(this, "Your address isn't found", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, "Your gps is disabled", Toast.LENGTH_SHORT).show();

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
        if (myOrder.size() == 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", 1);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public void computeTotalPrice() {
        deliveryFee.setText("€ " + restaurant.previewInfo.deliveryCost);
        double tp = 0.0;
        tp = myOrder.stream().mapToDouble(i -> i.price).sum() + restaurant.previewInfo.deliveryCost;

        if (restaurant.previewInfo.minOrderCost > tp) {
            double remainder = restaurant.previewInfo.minOrderCost - tp;
            tp += remainder;
            minOrder.setText("+" + remainder);
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
        if (myOrder.size() == 0) {
            return false;
        }
        if (where.getText().toString().equals("")) {
            where.setError(getResources().getString(R.string.invalid_address));
            return false;
        }
        if (when.getText().toString().equals("")) {
            when.setError(getResources().getString(R.string.invalid_time));
            return false;
        }
        if (checkedChip == null) {
            return false;
        }
        if (user.name == null || user.phoneNumber == null) {
            return false;
        }
        return true;
    }

}