package com.mad.delivery.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuActivity extends AppCompatActivity {
    private List<Integer> selectedItems = new ArrayList<>();
    private FloatingActionButton button;
    public static int LONG_PRESS_TIME = 500; // Time in miliseconds

    final Handler _handler = new Handler();
    Runnable _longPressed = new Runnable() {
        public void run() {
            Log.d("info","LongPress");
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                _handler.removeCallbacks(_longPressed);
                break;
            case MotionEvent.ACTION_UP:
                _handler.removeCallbacks(_longPressed);
                break;
        }
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.menuTool2);
        setSupportActionBar(toolbar);
        setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.menu_list);

        List<MenuItemRest> menuItems = Database.getInstance().getMenuItems();
        List<MenuItemRest> categoryItems = new ArrayList<>();

        button = findViewById(R.id.newMenuItem2);

        String category = getIntent().getStringExtra("category");

        if (category != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            for (MenuItemRest i : menuItems) {
                if (i.category.compareTo(category) == 0) {
                    categoryItems.add(i);
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(categoryItems, this));
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] integers = new int[selectedItems.size()];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = selectedItems.get(i);
                    }

                    Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                    intent.putExtra("menuitems", integers);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            button.hide();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemOffertRecyclerViewAdapter(menuItems, this, new IOnMenuItemSelected() {
                @Override
                public void OnMenuItemSelected(Integer id) {
                    if (selectedItems.contains(id)) {
                        selectedItems.remove(id);
                        if (selectedItems.isEmpty() && button.isShown()) {
                            button.hide();
                        }
                    } else {
                        selectedItems.add(id);
                        if (!selectedItems.isEmpty() && !button.isShown()) {
                            button.show();
                        }
                    }
                }
            }));
        }

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, toolbar.getLayoutParams().height, 0, 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("open", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newMenuItem2:

                Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

interface IOnMenuItemSelected {
    void OnMenuItemSelected(Integer id);
}