package com.mad.delivery.restaurant_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompletingOrderActivity extends AppCompatActivity implements TimePickerFragment.TimePickedListener {
    private Toolbar myToolBar;
    private Order modifiedOrder;
    private DateTime oldDateTime;
    private EditText adminNotes;
    private Spinner statusSpinner;
    CardView cvDeliveryOptions;
    TextView requestedDeliveryTime, currentStatus;
    Button btnDeliveryTimeChange, btnConfirm, btnAdd, btnUndoDelivery;
    private Order order;
    private List<OrderStatus> statusList;
    private ArrayAdapter<OrderStatus> spinnerAdapter;
    private AlertDialog confirmOrderDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completing_order);

        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);
        order = getIntent().getParcelableExtra("order");
        modifiedOrder = new Order(order);
        setTitle(getResources().getString(R.string.completing_order) + " " + modifiedOrder.id);
        statusList = getStatusAsList(modifiedOrder.status);
        requestedDeliveryTime = findViewById(R.id.tv_delivery_options_sentence);
        btnDeliveryTimeChange = findViewById(R.id.delivery_opt_change);
        btnConfirm = findViewById(R.id.delivery_opt_confirm);
        btnAdd = findViewById(R.id.delivery_admin_notes);
        currentStatus = findViewById(R.id.tv_current_statusValue);
        statusSpinner = findViewById(R.id.status_spinner);
        spinnerAdapter = new ArrayAdapter<OrderStatus>(this, R.layout.spinner_item, statusList);
        statusSpinner.setAdapter(spinnerAdapter);
        statusSpinner.setSelection(0, true);
        View v = statusSpinner.getSelectedView();
        ((TextView)v).setTextColor(getColor(OrderStatus.valueOf(((TextView)v).getText().toString())));
        ((TextView)v).setText(((TextView)v).getText().toString().toLowerCase());
        btnUndoDelivery = findViewById(R.id.btn_undo_delivery);
        requestedDeliveryTime.setText(getResources().getString(R.string.delivery_opt_sentence, MyDateFormat.parse(modifiedOrder.orderFor)));
        currentStatus.setText(modifiedOrder.status.toString().toLowerCase());
        currentStatus.setTextColor(getColor(modifiedOrder.status));
        cvDeliveryOptions = findViewById(R.id.cv_delivery_options);
        adminNotes = findViewById(R.id.et_admin_notes);
/*        LayoutTransition deliveryLt = ((ViewGroup) cvDeliveryOptions).getLayoutTransition();
        deliveryLt.setDuration(500);
        deliveryLt.enableTransitionType(LayoutTransition.CHANGING);*/

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
                modifiedOrder.orderFor = oldDateTime;
                requestedDeliveryTime.setText(getResources().getString(R.string.delivery_opt_sentence, MyDateFormat.parse(modifiedOrder.orderFor)));
                btnDeliveryTimeChange.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnUndoDelivery.setVisibility(View.GONE);
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

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OrderStatus selected = (OrderStatus) adapterView.getSelectedItem();
                modifiedOrder.status = selected;
                TextView selectedTextView = ((TextView)adapterView.getChildAt(0));
                selectedTextView.setTextColor(getColor(selected));
                selectedTextView.setText(selectedTextView.getText().toString().toLowerCase());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                confirmOrderDialog.show();
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
            case PENDING:
                return getResources().getColor(R.color.colorPendingOrder, null);
            case PREPARING:
                return getResources().getColor(R.color.colorPreparingOrder, null);
            case READY:
                return getResources().getColor(R.color.colorReadyOrder, null);
            case CANCELED:
                return getResources().getColor(R.color.colorCanceledOrder, null);
            case COMPLETED:
                return getResources().getColor(R.color.colorCompletedOrder, null);
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
        requestedDeliveryTime.setText(getResources().getString(R.string.confirmed_order, MyDateFormat.parse(modifiedOrder.orderFor)));
        btnDeliveryTimeChange.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
        btnUndoDelivery.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimePicked(int h, int m) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy hh:mm");
        modifiedOrder.orderFor = modifiedOrder.orderFor.hourOfDay().setCopy(h);
        modifiedOrder.orderFor = modifiedOrder.orderFor.minuteOfHour().setCopy(m);
        confirmDeliveryTime();
    }

    private AlertDialog createDialog(int titleResource, int messageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(messageResource)
                .setTitle(titleResource);

        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                modifiedOrder.serverNotes = adminNotes.getText().toString();
                modifiedOrder.status = (OrderStatus) statusSpinner.getSelectedItem();
                Log.d("MADAPP", "selected status: " + modifiedOrder.status.toString());
                order = modifiedOrder;
                Database.update(order);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        builder.setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

    public List<OrderStatus> getStatusAsList(OrderStatus initial) {
        List<OrderStatus> list = new ArrayList<>();
        switch(initial) {
            case PENDING:
                list.addAll(Arrays.asList(OrderStatus.PREPARING, OrderStatus.READY, OrderStatus.COMPLETED, OrderStatus.CANCELED));
                break;
            case PREPARING:
                list.addAll(Arrays.asList(OrderStatus.READY, OrderStatus.COMPLETED, OrderStatus.CANCELED));
                break;
            case READY:
                list.addAll(Arrays.asList(OrderStatus.COMPLETED));
                break;
            case COMPLETED:
                list.add(OrderStatus.COMPLETED);
                break;
            case CANCELED:
                list.add(OrderStatus.PENDING);
                break;
            default:
                list.addAll(Arrays.asList(OrderStatus.PENDING, OrderStatus.PREPARING, OrderStatus.READY, OrderStatus.COMPLETED, OrderStatus.CANCELED));
        }
        return list;
    }
}
