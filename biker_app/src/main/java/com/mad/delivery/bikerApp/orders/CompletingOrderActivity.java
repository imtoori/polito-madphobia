package com.mad.delivery.bikerApp.orders;

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
import com.mad.delivery.bikerApp.BikerDatabase;
import com.mad.delivery.bikerApp.HomeActivity;
import com.mad.delivery.bikerApp.auth.LoginActivity;
import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CompletingOrderActivity extends AppCompatActivity implements  ListDialog.ListDialogListener {
    private Toolbar myToolBar;
    private Order modifiedOrder;
    CardView cvDeliveryOptions;
    TextView requestedDeliveryTime, currentStatus, newStatus;
    TextView deliveryDone;

    Button btnChangeStatus;
    private Order order;
    private AlertDialog confirmOrderDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completing_order);
        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);
        order = getIntent().getParcelableExtra("order");
        modifiedOrder = new Order(order);
        mAuth = FirebaseAuth.getInstance();
        setTitle(getResources().getString(R.string.order) + " from " + order.client.name + " " +order.client.lastName.substring(0, 1) + ".");
        requestedDeliveryTime = findViewById(R.id.tv_delivery_options_sentence);
        deliveryDone = findViewById(R.id.tv_delivery_done);

        requestedDeliveryTime.setText(getResources().getString(R.string.delivery_opt_sentence, MyDateFormat.parse(new DateTime(modifiedOrder.orderFor))));
        cvDeliveryOptions = findViewById(R.id.cv_delivery_options);
        deliveryDone.setText(getResources().getString(R.string.delivery_done, MyDateFormat.parse(new DateTime())));

        confirmOrderDialog = createDialog(R.string.completing_order_title_dialog, R.string.completing_order_message_dialog);

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
        if(order.status.equals(OrderStatus.completed)) {
            getMenuInflater().inflate(R.menu.order_detail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_order_option:
                confirmOrderDialog.show();
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
            case completed:
                return getResources().getColor(R.color.colorCompletedOrder, null);
            case delivered:
                return getResources().getColor(R.color.colorDeliveredOrder, null);
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





    private AlertDialog createDialog(int titleResource, int messageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageResource)
                .setTitle(titleResource);
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //modifiedOrder.bikerId =order.bikerId;
                modifiedOrder.bikerId = order.bikerId;
                modifiedOrder.restaurantId = order.restaurantId;
                modifiedOrder.status = OrderStatus.delivered;
                Log.d("MADAPP", "selected status: " + modifiedOrder.status.toString());
                order = modifiedOrder;
                BikerDatabase.getInstance().update(order);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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
