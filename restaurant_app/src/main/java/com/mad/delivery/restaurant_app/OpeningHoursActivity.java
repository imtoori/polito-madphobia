package com.mad.delivery.restaurant_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpeningHoursActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Map<Integer, List<TimeRange>> openings;
    private Button btnAddTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_hours);
        recyclerView = findViewById(R.id.rl_hours);
        btnAddTime = findViewById(R.id.btn_add_opening);
        openings = new HashMap<>();
        List<TimeRange> forMonday = new ArrayList<>();
        openings.put(0, new ArrayList<>());
        openings.put(1, new ArrayList<>());
        openings.put(2, new ArrayList<>());
        openings.put(3, new ArrayList<>());
        openings.put(4, new ArrayList<>());
        openings.put(5, new ArrayList<>());
        openings.put(6, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DayOfWeekAdapter(openings));

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningHoursActivity.this);
                builder.setMessage(R.string.add_hour_dialog_message)
                        .setTitle(R.string.add_hour_dialog_title);

                builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.setView(R.layout.timerange_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("open", 2);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

}
