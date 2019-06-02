package com.mad.delivery.restaurant_app.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.OnFirebaseData;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PopularSettingActivity extends AppCompatActivity {
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    TextView first, second, third, first_q, second_q, third_q;
    ImageView first_img, second_img, third_img;

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
        first_q=findViewById(R.id.first_quantity);
        second_q=findViewById(R.id.second_quantity);
        third_q=findViewById(R.id.third_quantity);
        first_img =findViewById(R.id.img_first_place);
        second_img =findViewById(R.id.img_second_place);
        third_img =findViewById(R.id.img_third_place);

        RestaurantDatabase.getInstance().getPopularDish(new OnFirebaseData<TreeMap<String, Integer>>() {
            @Override
            public void onReceived(TreeMap<String, Integer> item) {
                List<String> listKeys = new ArrayList<String>(item.keySet());
                List<Integer> listValues = new ArrayList<Integer>(item.values());
                if(listKeys.size()>=1&&listValues.size()>=1){
                    first.setText(listKeys.get(0));
                    first_q.setText(listValues.get(0).toString());

                }
                else {
                    first.setVisibility(View.INVISIBLE);
                    first_q.setVisibility(View.INVISIBLE);
                    first_img.setVisibility(View.INVISIBLE);
                }
                if(listKeys.size()>=2&&listValues.size()>=2){
                    second.setText(listKeys.get(1));
                    second_q.setText(listValues.get(1).toString());

                }
                else {
                    second.setVisibility(View.INVISIBLE);
                    second_q.setVisibility(View.INVISIBLE);
                    second_img.setVisibility(View.INVISIBLE);

                }
                if(listKeys.size()>=3&&listValues.size()>=3){
                    third.setText(listKeys.get(2));
                    third_q.setText(listValues.get(2).toString());

                }
                else {
                    third.setVisibility(View.INVISIBLE);
                    third_q.setVisibility(View.INVISIBLE);
                    third_img.setVisibility(View.INVISIBLE);

                }
            }
        });

        findViewById(R.id.cv_daily_order).setVisibility(View.GONE);

        BarChart chart_timing = (BarChart) findViewById(R.id.barchart_timing);
        RestaurantDatabase.getInstance().getPopularTiming(new OnFirebaseData<List<BarEntry>>() {
            @Override
            public void onReceived(List<BarEntry> item) {
                List<BarEntry> timing = new ArrayList<>();
                timing.addAll(item);
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
