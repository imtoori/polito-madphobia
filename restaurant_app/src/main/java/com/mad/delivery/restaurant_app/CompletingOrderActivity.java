package com.mad.delivery.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CompletingOrderActivity extends AppCompatActivity {
    private Toolbar myToolBar;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completing_order);
        myToolBar = findViewById(R.id.detailToolbar);
        setSupportActionBar(myToolBar);

        order = getIntent().getParcelableExtra("order");
        setTitle(getResources().getString(R.string.completing_order) + " " + order.id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
        intent.putExtra("order", order);
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
        switch(item.getItemId()) {
            case R.id.save_order_option:
                Log.d("MADAPP", "Save option selected");
                Intent intent = new Intent(getApplicationContext(), CompletingOrderActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.reject_order_option:
                Log.d("MADAPP", "Reject option selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
