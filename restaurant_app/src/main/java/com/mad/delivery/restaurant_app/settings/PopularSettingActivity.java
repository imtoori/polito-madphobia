package com.mad.delivery.restaurant_app.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

public class PopularSettingActivity extends AppCompatActivity {
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    TextView first, second, third, first_q, second_q, third_q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);
        myToolbar = (Toolbar) findViewById(R.id.popular_toolbar);
        setTitle(getResources().getString(R.string.popular_toolbar));
        setSupportActionBar(myToolbar);
        mAuth = FirebaseAuth.getInstance();
        first= findViewById(R.id.tv_first_place);
        second=findViewById(R.id.tv_second_place);
        third=findViewById(R.id.tv_third_place);
        first.setText("Pasta al sugo");
        second.setText("Parmigiana");
        third.setText("Polpette");
        //TODO: SETTARE IL NOME PIATTO IN FIRST, SECOND , THIRD
        first_q=findViewById(R.id.first_quantity);
        second_q=findViewById(R.id.second_quantity);
        third_q=findViewById(R.id.third_quantity);
        first_q.setText("12");
        second_q.setText("44");
        third_q.setText("120");
        //TODO: SETTARE IN FIRST_Q, SECOND_Q , THIRD_Q LE QIANTITà VENDUTE DEI RISPETTIVI PIATTI
        findViewById(R.id.cv_daily_order).setVisibility(View.GONE);
/*
        BarChart chart  = (BarChart) findViewById(R.id.barchart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 30f));
        entries.add(new BarEntry(1, 80f));
        entries.add(new BarEntry(2, 60f));
        entries.add(new BarEntry(3, 50f));
        entries.add(new BarEntry(4, 50f));
        entries.add(new BarEntry(5, 70f));
        entries.add(new BarEntry(6, 60f));

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("L");
        xLabel.add("M");
        xLabel.add("M");
        xLabel.add("G");
        xLabel.add("V");
        xLabel.add("S");
        xLabel.add("D");


        BarDataSet set = new BarDataSet(entries, "");
        set.setStackLabels(new String[]{"L", "M","L", "M","L", "M","L"});
        set.setColors(Color.RED);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Log.i("MADAPP", "data X" + xLabel);
                return xLabel.get((int) value );
            }
        });


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

*/
        //---------------------------------------

        BarChart chart_timing = (BarChart) findViewById(R.id.barchart_timing);

        //TODO: INSERIRE IN TIMING COPPIE (ORA, VALORE) !!VUOLE DEI FLOAT
        List<BarEntry> timing = new ArrayList<>();
        timing.add(new BarEntry(0f, 30f));
        timing.add(new BarEntry(1f, 75f));
        timing.add(new BarEntry(8f, 60f));
        timing.add(new BarEntry(12f, 50f));
        timing.add(new BarEntry(15f, 50f));
        timing.add(new BarEntry(18f, 70f));
        timing.add(new BarEntry(23f, 60f));


        BarDataSet set_timing = new BarDataSet(timing, "Number of Order");
        set_timing.setColors(Color.RED);
        BarData data_timing = new BarData(set_timing);
        data_timing.setBarWidth(0.9f); // set custom bar width
        chart_timing.setData(data_timing);
        chart_timing.setFitBars(true); // make the x-axis fit exactly all bars
        chart_timing.invalidate(); // refresh
        chart_timing.setDrawBarShadow(false);
        chart_timing.setDrawValueAboveBar(true); //visualizza valore sopra

        XAxis xAxis_timing = chart_timing.getXAxis();
        xAxis_timing.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_timing.setDrawGridLines(false);
        xAxis_timing.setGranularity(1f);
        xAxis_timing.setLabelCount(12);


        YAxis leftAxis_timing = chart_timing.getAxisLeft();
        leftAxis_timing.setLabelCount(8, false);
        leftAxis_timing.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis_timing.setSpaceTop(15f);
        leftAxis_timing.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart_timing.getAxisRight().setEnabled(false);
        chart_timing.getLegend().setEnabled(false);
        chart_timing.getDescription().setEnabled(false);


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

        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant user) {
                // do nothing
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("open", 2);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



}
