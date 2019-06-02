package com.mad.delivery.consumerApp.search;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.auth.LoginActivity;
import com.mad.delivery.consumerApp.search.Order.BasketActivity;
import com.mad.delivery.consumerApp.search.Order.OnProductListener;
import com.mad.delivery.consumerApp.search.Order.ProductsAdapter;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.OnImageDownloaded;
import com.mad.delivery.resources.OnLogin;
import com.mad.delivery.resources.PreviewInfo;
import com.mad.delivery.resources.Product;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RestaurantInfoActivity extends AppCompatActivity implements MenuItemAdapter.OnItemSelected, OnProductListener {
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Restaurant restaurant;
    private FrameLayout redCircle;
    private TextView countTextView;
    private int itemsNumber = 0;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button btnNext;
    private User user;
    AlertDialog dialog;
    Map<String, Product> myOrder;
    List<Product> orderList;
    private ProductsAdapter myOrderAdapter;
    private PreviewInfo previewInfo;

    // dialog items
    TextView deliveryFee, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        myOrder = new HashMap<>();
        appBarLayout = findViewById(R.id.app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = findViewById(R.id.restaurant_pager);
        tabLayout = findViewById(R.id.tab_restaurant_header);
        try {
            Bundle bundle = getIntent().getExtras();
            previewInfo = (PreviewInfo) bundle.get("restaurant");
            loadRestaurantInfo(false);

        } catch (NullPointerException e) {
            // do nothing
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        ConsumerDatabase.getInstance().checkLogin(currentUser.getUid(), new OnLogin<User>() {
            @Override
            public void onSuccess(User u) {
                user = u;
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.option_shopping);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        rootView.setOnClickListener(v -> {
            openShoppingCartPreview();
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_info, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.i("MADAPP", "onSupportNavigateUp");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.i("MADAPP", "onBackPressed");
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void updateShoppingCartIcon(int n) {
        countTextView.setText(String.valueOf(n));
        redCircle.setVisibility((n > 0) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void itemAdded(MenuItemRest item) {
        itemsNumber++;
        updateShoppingCartIcon(itemsNumber);
        if (myOrder.containsKey(item.id)) {
            Product p = myOrder.get(item.id);
            p.addProduct(item, 1);
        } else {
            myOrder.put(item.id, new Product(item, 1));
        }
    }

    @Override
    public void itemRemoved(MenuItemRest item) {
        itemsNumber--;
        if (itemsNumber < 0) itemsNumber = 0;
        updateShoppingCartIcon(itemsNumber);
        if (myOrder.containsKey(item.id)) {
            Product p = myOrder.get(item.id);
            p.delProduct(item, 1);
            if (p.quantity == 0) myOrder.remove(p);
        }
    }

    public void openShoppingCartPreview() {
        if (myOrder.size() == 0) {
            AlertDialog.Builder emptyBuilder = new AlertDialog.Builder(this);
            emptyBuilder.setMessage(R.string.dialog_shopping_cart_empty)
                    .setTitle(R.string.dialog_shopping_cart_preview)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog emptyCartDialog = emptyBuilder.create();
            emptyCartDialog.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View convertView = LayoutInflater.from(this).inflate(R.layout.dialog_shoppingcart_preview, null);
        deliveryFee = convertView.findViewById(R.id.preview_deliverycost_value);
        totalPrice = convertView.findViewById(R.id.preview_totalprice);
        Button btnLogin = convertView.findViewById(R.id.btn_login);
        btnNext = convertView.findViewById(R.id.btn_next);
        Button btnBack = convertView.findViewById(R.id.btn_back);
        if (user == null) {
            btnLogin.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        } else {
            btnLogin.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        }
        computeTotalPrice();
        builder.setView(convertView);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                btnBack.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                btnNext.setOnClickListener(v -> {
                    if (user.name == null && user.lastName == null && user.phoneNumber == null) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantInfoActivity.this);
                        builder.setTitle("Error")
                                .setMessage("Your profile is incomplete. Please go to My Account -> Profile. ")
                                .setPositiveButton("I understand", (dialogInterface1, i) -> {
                                    dialogInterface1.dismiss();
                                }).create().show();
                    } else {
                        Intent intent = new Intent(RestaurantInfoActivity.this, BasketActivity.class);
                        intent.putExtra("myOrder", (Serializable) myOrder);
                        intent.putExtra("restaurant", (Parcelable) restaurant);
                        startActivity(intent);
                    }
                });
                btnLogin.setOnClickListener(v -> {
                    Intent intent = new Intent(RestaurantInfoActivity.this, LoginActivity.class);
                    intent.putExtra("myOrder", (Serializable) myOrder);
                    startActivity(intent);
                });
            }
        });

        RecyclerView previewCartRecyclerView = convertView.findViewById(R.id.cartRecyclerView);
        previewCartRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        orderList = new ArrayList<>(myOrder.values());
        myOrderAdapter = new ProductsAdapter(orderList, this);
        previewCartRecyclerView.setAdapter(myOrderAdapter);
        previewCartRecyclerView.hasFixedSize();
        dialog.show();
    }

    @Override
    public void onRemoved(Product p) {
        orderList.removeIf(item -> item.idItem.equals(p.idItem));
        myOrder.remove(p.idItem);
        myOrderAdapter.notifyDataSetChanged();
        computeTotalPrice();
        itemsNumber -= p.quantity;
        updateShoppingCartIcon(itemsNumber);
        if (myOrder.size() == 0) {
            dialog.dismiss();
            loadRestaurantInfo(true);
        }
    }

    public void computeTotalPrice() {
        deliveryFee.setText("€ " + restaurant.previewInfo.deliveryCost);
        double tp = 0.0;
        tp = myOrder.entrySet().stream().mapToDouble(i -> i.getValue().price).sum() + restaurant.previewInfo.deliveryCost;

        if (restaurant.previewInfo.minOrderCost > tp) {
            double remainder = restaurant.previewInfo.minOrderCost - tp;
            tp += remainder;
        }
        totalPrice.setText("€ " + tp);

    }

    private void loadRestaurantInfo(boolean restaurantMenu) {
        ConsumerDatabase.getInstance().getRestaurantInfo(previewInfo, (rest) -> {
            restaurant = rest;
            if (restaurant.previewInfo.imageName == null || restaurant.previewInfo.imageName.equals("")) {
                appBarLayout.setBackground(getDrawable(R.drawable.restaurant_default));
            } else {
                ConsumerDatabase.getInstance().downloadImage(restaurant.previewInfo.id, "profile", restaurant.previewInfo.imageName, new OnImageDownloaded() {
                    @Override
                    public void onReceived(Uri imageUri) {
                        if (imageUri == null || imageUri == Uri.EMPTY) {
                            appBarLayout.setBackground(getDrawable(R.drawable.restaurant_default));
                        } else {
                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setImageBitmap(bitmap);
                                    Drawable image = imageView.getDrawable();
                                    appBarLayout.setBackground(image);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {}
                            };
                            Picasso.get().load(imageUri).into(target);

                        }
                    }
                });
            }

            pagerAdapter = new RestaurantInfoPageAdapter(getSupportFragmentManager(), this, restaurant);
            mPager.setAdapter(pagerAdapter);
            collapsingToolbarLayout.setTitle(restaurant.previewInfo.name);
            // Give the TabLayout the ViewPager

            tabLayout.setupWithViewPager(mPager);
            if (restaurantMenu) {
                tabLayout.selectTab(tabLayout.getTabAt(1));
            }
        });
    }
}
