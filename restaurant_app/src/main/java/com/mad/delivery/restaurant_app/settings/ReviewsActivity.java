package com.mad.delivery.restaurant_app.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.auth.LoginActivity;
import com.mad.delivery.restaurant_app.auth.OnLogin;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
    Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView totalReviewsCount, monthlyReviewsCount, weeklyReviewsCount;
    private TextView foodVote, serviceVote;
    private RatingBar ratingBar;
    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Feedback> reviews;
    int total, monthly, weekly;
    float foodSum, servicesSum;
    float foodMean, servicesMean, overallMean;
    DateTime today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        myToolbar = (Toolbar) findViewById(R.id.reviews_toolbar);
        setTitle(getResources().getString(R.string.reviews));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        total = 0; monthly = 0; weekly = 0;
        foodSum = 0; servicesSum = 0;
        foodMean = 0; servicesMean = 0; overallMean = 0;
        today = new DateTime();

        totalReviewsCount = findViewById(R.id.tv_amount_total);
        monthlyReviewsCount = findViewById(R.id.tv_amount_monthly);
        weeklyReviewsCount = findViewById(R.id.tv_amount_weekly);
        foodVote = findViewById(R.id.tv_rate);
        serviceVote = findViewById(R.id.tv_service_rate);
        ratingBar = findViewById(R.id.rate_food);
        recyclerView = findViewById(R.id.rv_reviews);
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        RestaurantDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<Restaurant>() {
            @Override
            public void onSuccess(Restaurant rest) {
                // get the reviews
                RestaurantDatabase.getInstance().getReviews(rest.previewInfo.id, r -> {
                    total++;
                    DateTime rDate = new DateTime(r.date);
                    if(rDate.getYear() == today.getYear() && rDate.getMonthOfYear() == today.getMonthOfYear()) {
                        monthly++;
                        if(rDate.getDayOfWeek() == today.getDayOfWeek()) weekly++;
                    }
                    foodSum +=  r.foodVote.floatValue();
                    servicesSum += r.serviceRestaurantVote.floatValue();
                    foodMean = foodSum / total;
                    servicesMean = servicesSum / total;
                    overallMean = (foodSum + servicesSum) / total;

                    totalReviewsCount.setText(String.valueOf(total));
                    monthlyReviewsCount.setText(String.valueOf(monthly));
                    weeklyReviewsCount.setText(String.valueOf(weekly));
                    foodVote.setText(foodMean+"/5.0");
                    serviceVote.setText(servicesMean+"/5.0");
                    ratingBar.setRating(overallMean);

                    reviews.add(r);
                    reviewsAdapter.notifyDataSetChanged();
                });

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
