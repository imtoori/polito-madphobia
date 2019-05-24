package com.mad.delivery.restaurant_app.orders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.FireBaseCallBack;
import com.mad.delivery.restaurant_app.ListDialog;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.MyDateFormat;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.TimePickerFragment;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Random;

public class CompletingOrderActivity extends AppCompatActivity implements TimePickerFragment.TimePickedListener, ListDialog.ListDialogListener {
    private Toolbar myToolBar;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private Order modifiedOrder;
    private DateTime oldDateTime;
    private EditText adminNotes;
    CardView cvDeliveryOptions, cvAdminNotes, cvChangeStatus;
    TextView requestedDeliveryTime, currentStatus, newStatus, confirmError;
    ImageView imageConfirmError;
    Button btnDeliveryTimeChange, btnConfirm, btnAdd, btnUndoDelivery, btnChangeStatus;
    private Order order;
    private AlertDialog confirmOrderDialog;
    private boolean orderIsConfirmed;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        setContentView(R.layout.activity_completing_order);
        orderIsConfirmed = false;
        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);
        mAuth = FirebaseAuth.getInstance();
        order = getIntent().getParcelableExtra("order");
        modifiedOrder = new Order(order);
        setTitle(getResources().getString(R.string.completing_order) + " " + modifiedOrder.id);
        requestedDeliveryTime = findViewById(R.id.tv_delivery_options_sentence);
        btnDeliveryTimeChange = findViewById(R.id.delivery_opt_change);
        btnConfirm = findViewById(R.id.delivery_opt_confirm);
        btnAdd = findViewById(R.id.delivery_admin_notes);
        currentStatus = findViewById(R.id.tv_current_statusValue);
        newStatus = findViewById(R.id.tv_new_status);
        confirmError = findViewById(R.id.tv_confirm_delivery_error);
        imageConfirmError = findViewById(R.id.img_confirm_delivery_error);
        String newStatusAsString = ListDialog.getStatusAsList(modifiedOrder.status).get(0);
        newStatus.setText(newStatusAsString);
        newStatus.setTextColor(getColor(OrderStatus.valueOf(newStatusAsString)));
        btnUndoDelivery = findViewById(R.id.btn_undo_delivery);
        requestedDeliveryTime.setText(getResources().getString(R.string.delivery_opt_sentence, MyDateFormat.parse(DateTime.parse(modifiedOrder.orderFor))));
        currentStatus.setText(modifiedOrder.status.toString().toLowerCase());
        currentStatus.setTextColor(getColor(modifiedOrder.status));
        cvDeliveryOptions = findViewById(R.id.cv_delivery_options);
        cvAdminNotes  = findViewById(R.id.cv_admin_notes);
        cvChangeStatus = findViewById(R.id.cv_status_change);
        adminNotes = findViewById(R.id.et_admin_notes);
        if(order.serverNotes != null && !order.serverNotes.equals("")) adminNotes.setText(order.serverNotes);
        btnChangeStatus = findViewById(R.id.btn_change_status);
        LayoutTransition adminNotesLt =  cvAdminNotes.getLayoutTransition();
        adminNotesLt.setDuration(500);
        adminNotesLt.enableTransitionType(LayoutTransition.CHANGING);

        LayoutTransition deliveryLt =  cvDeliveryOptions.getLayoutTransition();
        deliveryLt.setDuration(500);
        deliveryLt.enableTransitionType(LayoutTransition.CHANGING);

        LayoutTransition changeStatusLt =  cvChangeStatus.getLayoutTransition();
        changeStatusLt.setDuration(500);
        changeStatusLt.enableTransitionType(LayoutTransition.CHANGING);

        oldDateTime = new DateTime(modifiedOrder.orderFor);
        confirmOrderDialog = createDialog(R.string.completing_order_title_dialog, R.string.completing_order_message_dialog);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeliveryTime();
            }
        });
        btnUndoDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifiedOrder.orderFor = oldDateTime.toString();
                requestedDeliveryTime.setText(getResources().getString(R.string.delivery_opt_sentence, MyDateFormat.parse(DateTime.parse(modifiedOrder.orderFor))));
                btnDeliveryTimeChange.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnUndoDelivery.setVisibility(View.GONE);
                orderIsConfirmed = false;

            }
        });
        btnDeliveryTimeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd.setVisibility(View.GONE);
                adminNotes.setVisibility(View.VISIBLE);
            }
        });

        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new ListDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentStatus", modifiedOrder.status);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "listStatus");
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant user) {
                // nothing happens..
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
        Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
        intent.putExtra("order", modifiedOrder);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_completing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_order:
                if(orderIsConfirmed) {
                    confirmOrderDialog.show();
                } else {
                    confirmError.setVisibility(View.VISIBLE);
                    imageConfirmError.setVisibility(View.VISIBLE);
                    final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake_effect);
                    animShake.setInterpolator(new AccelerateDecelerateInterpolator());
                    cvDeliveryOptions.startAnimation(animShake);
                }
                return true;
            case R.id.reject_order_option:
                Log.d("MADAPP", "Reject option selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getColor(OrderStatus st) {
        switch (st) {
            case pending:
                return getResources().getColor(R.color.colorPendingOrder, null);
            case preparing:
                return getResources().getColor(R.color.colorPreparingOrder, null);
            case ready:
                return getResources().getColor(R.color.colorReadyOrder, null);
            case canceled:
                return getResources().getColor(R.color.colorCanceledOrder, null);
            case completed:
                return getResources().getColor(R.color.colorCompletedOrder, null);
            case delivered:
                return  getResources().getColor(R.color.colorDeliveredOrder, null);
            default:
                return getResources().getColor(R.color.colorCanceledOrder, null);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("datetime", modifiedOrder.orderFor);
        timeFragment.setArguments(bundle);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void confirmDeliveryTime() {
        requestedDeliveryTime.setText(getResources().getString(R.string.confirmed_order, MyDateFormat.parse(DateTime.parse(modifiedOrder.orderFor))));
        btnDeliveryTimeChange.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
        btnUndoDelivery.setVisibility(View.VISIBLE);
        confirmError.setVisibility(View.GONE);
        imageConfirmError.setVisibility(View.GONE);
        orderIsConfirmed = true;
    }

    @Override
    public void onTimePicked(int h, int m) {
         DateTime.parse(modifiedOrder.orderFor).hourOfDay().setCopy(h);
        DateTime.parse(modifiedOrder.orderFor).minuteOfHour().setCopy(m);
        confirmDeliveryTime();
    }

    private AlertDialog createDialog(int titleResource, int messageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageResource)
                .setTitle(titleResource);
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                modifiedOrder.serverNotes = adminNotes.getText().toString();

                if(newStatus.getText().toString().equals("preparing")){
                    Random rand = new Random();

                    RestaurantDatabase.getInstance().getBikerId(new FireBaseCallBack<String>() {
                        @Override
                        public void onCallback(String user) {

                            modifiedOrder.bikerId = user;

                            modifiedOrder.restaurantId =order.restaurantId;
                            myRef.child("orders").child(modifiedOrder.id).child("bikerId").setValue(modifiedOrder.bikerId);
                            modifiedOrder.status = OrderStatus.valueOf(newStatus.getText().toString());
                            Log.d("MADAPP", "selected status: " + modifiedOrder.status.toString());
                            order = modifiedOrder;
                            RestaurantDatabase.getInstance().update(modifiedOrder);
                        }

                        @Override
                        public void onCallbackList(List<String> list) {

                        }
                    });
                }
                else{
                    modifiedOrder.status = OrderStatus.valueOf(newStatus.getText().toString());
                    Log.d("MADAPP", "selected status: " + modifiedOrder.status.toString());
                    order = modifiedOrder;
                    RestaurantDatabase.getInstance().update(modifiedOrder);

                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onElementChoosen(OrderStatus s) {
        newStatus.setTextColor(getColor(s));
        newStatus.setText(s.toString());
    }
}
