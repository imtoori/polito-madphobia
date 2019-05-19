package com.mad.delivery.restaurant_app.menu;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MenuOffer;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.restaurant_app.OnFirebaseData;
import com.mad.delivery.restaurant_app.RestaurantDatabase;
import com.mad.delivery.restaurant_app.OnDataFetched;
import com.mad.delivery.restaurant_app.R;
import com.mad.delivery.restaurant_app.auth.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MenuFragment extends Fragment implements OnMenuChanged {
    private RecyclerView recyclerView, offersRecyclerView;
    private MyMenuItemCategoryRecyclerViewAdapter adapter;
    private Map<String, List<MenuItemRest>> menu;
    private List<String> categories;
    private CardView cvEmpty, cvHeader, cvOffer;
    private Restaurant restaurant;
    private Button btnNewDish, btnNewOffer, btnCompleteOffer, btnCancelOffer;
    private TextView offerCount, tvOffersTitle, tvNoOffer;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<MenuItemRest> offer;
    private List<MenuOffer> myOffers;
    private OffersListAdapter myOffersListAdapter;
    private OfferAdapter myOfferAdapter;
    private AlertDialog dialog, errorDialog;
    private static boolean offerMode = false;
    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        recyclerView = v.findViewById(R.id.rv_menu);
        offersRecyclerView = v.findViewById(R.id.rv_offers);
        tvOffersTitle = v.findViewById(R.id.tv_offers);
        btnNewDish = v.findViewById(R.id.btn_new_dish);
        btnNewOffer = v.findViewById(R.id.btn_new_offer);
        cvHeader = v.findViewById(R.id.header_cardview);
        cvOffer = v.findViewById(R.id.offer_cardview);
        offerCount = v.findViewById(R.id.tv_offer_count);
        btnCompleteOffer = v.findViewById(R.id.btn_complete_offer);
        btnCancelOffer = v.findViewById(R.id.btn_cancel_offer);
        cvEmpty = v.findViewById(R.id.empty_cardview);
        tvNoOffer = v.findViewById(R.id.tv_no_offer);


        // offer is used to keep track of the offer you want to add
        offer = new ArrayList<>();
        myOfferAdapter = new OfferAdapter(offer);

        // this adapter is used to keep track of menu and categories
        menu = new HashMap<>();
        categories = new ArrayList<>();
        adapter = new MyMenuItemCategoryRecyclerViewAdapter(menu, categories, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.hasFixedSize();

        // myOffers contains all offers for this restaurant
        myOffers = new ArrayList<>();
        myOffersListAdapter = new OffersListAdapter(myOffers, this);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        offersRecyclerView.setAdapter(myOffersListAdapter);

        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getActivity());
        errorBuilder.setMessage(R.string.no_item_offer)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        errorDialog = errorBuilder.create();

        /*
        * When the fragment is created, offer mode is disabled.
        */
        offerCount.setText(getResources().getString(R.string.offer_count, String.valueOf(0)));
        offerMode = false;

        // create a new dish
        btnNewDish.setOnClickListener(view -> {
            openNewDishActivity(null);
        });

        // enter in offer mode
        btnNewOffer.setOnClickListener( view -> {
            offer.clear();
            updateOfferItemsCount();
            myOfferAdapter.notifyDataSetChanged();
            cvOffer.setVisibility(View.VISIBLE);
            cvHeader.setVisibility(View.GONE);
            tvOffersTitle.setVisibility(View.GONE);
            offersRecyclerView.setVisibility(View.GONE);
            tvNoOffer.setVisibility(View.GONE);
            offerMode = true;
            loadRestaurantMenu();
        });
        btnCompleteOffer.setOnClickListener(view -> {
            if(offer.size() != 0) {
                createNewOffer();
            } else {
                errorDialog.show();
            }

        });
        btnCancelOffer.setOnClickListener(view -> {
            cvOffer.setVisibility(View.GONE);
            cvHeader.setVisibility(View.VISIBLE);
            tvOffersTitle.setVisibility(View.VISIBLE);
            offersRecyclerView.setVisibility(View.VISIBLE);
            offer.clear();
            myOfferAdapter.notifyDataSetChanged();
            offerMode = false;
            loadRestaurantMenu();
        });


        loadRestaurantMenu();

        RestaurantDatabase.getInstance().getOffers(currentUser.getUid(), item -> {
                myOffers.addAll(item);
                myOffersListAdapter.notifyDataSetChanged();
                if(myOffers.size() == 0) {
                    tvNoOffer.setVisibility(View.VISIBLE);
                } else {
                    tvNoOffer.setVisibility(View.GONE);
                }
        });




        return v;
    }

    @Override
    public void offerRemoved(MenuOffer item) {
        RestaurantDatabase.getInstance().removeMenuOffer(item.restaurantID, item, new OnFirebaseData<MenuOffer>() {
            @Override
            public void onReceived(MenuOffer item) {
                myOffers.removeIf(i -> i.id.equals(item.id));
                myOffersListAdapter.notifyDataSetChanged();
                if(myOffers.size() == 0) {
                    tvNoOffer.setVisibility(View.VISIBLE);
                } else {
                    tvNoOffer.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    public void itemRemoved(MenuItemRest item) {
        RestaurantDatabase.getInstance().removeMenuItem(item.restaurantId, item, new OnMenuReceived() {
            @Override
            public void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories) {
                // nothing
            }
            @Override
            public void itemRemoved(MenuItemRest item) {
                List<MenuItemRest> menuList = menu.get(item.category);
                if (menuList != null) {
                    menuList.removeIf(i -> i.name.equals(item.name));
                    if (menuList.size() == 0) {
                        menu.remove(item.category);
                        categories.removeIf(i -> item.category.equals(i));
                    }
                    adapter.notifyDataSetChanged();
                }
                if (menu.size() == 0) {
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
        /*if(offerMode) {
            Log.d("MADAPP", "removing element from offer if exists");
            offer.removeIf(i -> i.id.equals(item.id));
            updateOfferItemsCount();
        }*/
    }

    @Override
    public void itemSelected(MenuItemRest item) {
        openNewDishActivity(item);
    }

    @Override
    public void addedToOffer(MenuItemRest item) {
        offer.add(item);
        updateOfferItemsCount();
    }
    @Override
    public void removedFromOffer(MenuItemRest item) {
        offer.removeIf(i -> i.id.equals(item.id));
        updateOfferItemsCount();
    }


    public void openNewDishActivity(MenuItemRest item) {
        Intent intent = new Intent(getContext(), NewMenuItemActivity.class);
        ArrayList<String> menuList = new ArrayList<>(menu.keySet());
        intent.putStringArrayListExtra("categories", menuList);
        if(item != null) {
            intent.putExtra("item", (Parcelable) item);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void createNewOffer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_offer_layout, null);
        EditText price = convertView.findViewById(R.id.complete_offer_price);
        EditText time = convertView.findViewById(R.id.complete_offer_time);
        builder.setView(convertView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadRestaurantMenu();

                    }
                });
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(v -> {
                    boolean error = false;
                    if(price.getText().toString().equals("") || Double.valueOf(price.getText().toString()) <= 0.00) {
                        price.setError("Add a price");
                        error = true;
                    }
                    if(time.getText().toString().equals("") || Integer.valueOf(time.getText().toString()) <= 0.00) {
                        time.setError("Add a valid expiring time");
                        error = true;
                    }
                    if(error) {
                        return;
                    }
                    List<MenuItemRest> offerToSave = new ArrayList<>();
                    offerToSave.addAll(offer);
                    saveOffer(currentUser.getUid(), offerToSave, price.getText().toString(), time.getText().toString());
                    offerMode = false;
                    cvOffer.setVisibility(View.GONE);
                    cvHeader.setVisibility(View.VISIBLE);
                    tvOffersTitle.setVisibility(View.VISIBLE);
                    offersRecyclerView.setVisibility(View.VISIBLE);
                    offer.clear();
                    updateOfferItemsCount();
                    dialog.dismiss();
                });
            }
        });





        RecyclerView offerRecyclerView = convertView.findViewById(R.id.offerRecyclerview);
        offerRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        offerRecyclerView.setAdapter(myOfferAdapter);
        offerRecyclerView.hasFixedSize();
        dialog.show();
    }

    public void updateOfferItemsCount() {
        offerCount.setText(getResources().getString(R.string.offer_count, String.valueOf(offer.size())));
    }

    public static boolean offerIsEnabled() {
        return offerMode;
    }

    public void saveOffer(String restaurantID, List<MenuItemRest> offer, String price, String time) {
        if(restaurantID == null) return;
        Double p = Double.valueOf(price);
        Integer t = Integer.valueOf(time);
        MenuOffer menuOffer = new MenuOffer(currentUser.getUid(), offer,  p, t);
        RestaurantDatabase.getInstance().updateOffer(restaurantID, menuOffer, item -> {
            myOffers.add(menuOffer);
            myOffersListAdapter.notifyDataSetChanged();
            loadRestaurantMenu();
            if(myOffers.size() == 0) {
                tvNoOffer.setVisibility(View.VISIBLE);
            } else {
                tvNoOffer.setVisibility(View.GONE);
            }
        });
    }

    public void loadRestaurantMenu() {
        RestaurantDatabase.getInstance().getMenu(currentUser.getUid(), new OnMenuReceived() {
            @Override
            public void menuReceived(Map<String, List<MenuItemRest>> menu, List<String> categories) {
                MenuFragment.this.menu.putAll(menu);
                MenuFragment.this.categories.addAll(categories);
                adapter.notifyDataSetChanged();
                if(menu.size() != 0) {
                    cvEmpty.setVisibility(View.GONE);
                } else {
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void itemRemoved(MenuItemRest item) {
                // nothing happens
            }
        });
    }
}
