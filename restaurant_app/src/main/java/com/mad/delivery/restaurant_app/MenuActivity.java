package com.mad.delivery.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

        FloatingActionButton botton = findViewById(R.id.newMenuItem2);
        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        String category = getIntent().getStringExtra("category");

        if (category != null) {
            for (MenuItemRest i : menuItems) {
                if (i.category.compareTo(category) == 0) {
                    categoryItems.add(i);
                }
            }


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(categoryItems, this));
        } else {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemOffertRecyclerViewAdapter(menuItems, this, new IOnMenuItemSelected() {
                @Override
                public void OnMenuItemSelected(Integer id) {
                    if (selectedItems.contains(id)) {
                        selectedItems.remove(id);
                    } else {
                        selectedItems.add(id);
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