package com.mad.delivery.restaurant_app.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.restaurant_app.Database;
import com.mad.delivery.restaurant_app.MainActivity;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuActivity extends AppCompatActivity {
    private List<String> selectedItems = new ArrayList<>();
    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.menuTool2);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.menu_list);

        Database.getInstance().getMenuItems(new OnDataFetched<List<MenuItemRest>, String>() {
            @Override
            public void onDataFetched(List<MenuItemRest> data) {

                loadData(data);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MenuActivity.this, "Error reading data: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, toolbar.getLayoutParams().height, 0, 0);
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

    void loadData(List<MenuItemRest> menuItems) {
        List<MenuItemRest> categoryItems = new ArrayList<>();

        button = findViewById(R.id.newMenuItem2);

        String category = getIntent().getStringExtra("category");

        if (category != null) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
            for (MenuItemRest i : menuItems) {
                if (i.category.compareTo(category) == 0) {
                    categoryItems.add(i);
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(categoryItems, this));
        } else {
            button.setOnClickListener(v -> {
                String[] strings = new String[selectedItems.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = selectedItems.get(i);
                }

                Intent intent = new Intent(getApplicationContext(), NewMenuItemActivity.class);
                intent.putExtra("menuitems", strings);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
            button.hide();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyMenuItemOffertRecyclerViewAdapter(menuItems, this, new IOnMenuItemSelected() {
                @Override
                public void OnMenuItemSelected(String id) {
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
    void OnMenuItemSelected(String id);
}