package com.mad.delivery.consumerApp.search;

import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.GPSTracker;
import com.mad.delivery.consumerApp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements CategoriesFragment.OnCategorySelected {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ImageButton search;
    private ImageButton location;
    private CategoriesFragment catFragment;
    private RestaurantsFragment restaurantsFragment;
    private CardView filter;
    private Chip delivery, minorder;
    private ChipGroup chipGroup;
    private String address = "";
    private EditText deliveryAddress;
    private Set<String> chosen;
    private Map<String, Chip> chipMap;

    private boolean freeDelivery, minOrderCost;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
        search = view.findViewById(R.id.search_imgbtn);
        filter = view.findViewById(R.id.cv_filters);
        delivery = view.findViewById(R.id.deliverychip);
        minorder = view.findViewById(R.id.minorderchip);
        chipGroup = view.findViewById(R.id.chip_group);
        location = view.findViewById(R.id.location_img);
        deliveryAddress = view.findViewById(R.id.delivery_address_et);
        chipMap = new HashMap<>();
        chosen = new HashSet<>();
        freeDelivery = false;
        minOrderCost = false;

        delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                applyFilters(minOrderCost, b);
                if (b) {
                    delivery.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    delivery.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    delivery.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    freeDelivery = true;
                } else {
                    delivery.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    delivery.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    delivery.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    freeDelivery = false;
                }
            }
        });

        minorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                applyFilters(b, freeDelivery);
                if (b) {
                    minOrderCost = true;
                    minorder.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    minorder.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    minorder.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                } else {
                    minOrderCost = false;
                    minorder.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    minorder.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    minorder.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategory(null, "", minOrderCost, freeDelivery);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gps = new GPSTracker(getContext());
                if (gps.isGPSEnabled) {
                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<>();
                    geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        Double latitude = gps.getLatitude();
                        Double longitude = gps.getLongitude();
                        Log.d("latitude: ", latitude.toString());
                        Log.d("longitude: ", longitude.toString());

                        addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        String city = addresses.get(0).getLocality();
                        String address = addresses.get(0).getThoroughfare() + ", " + addresses.get(0).getFeatureName();
                        String country = addresses.get(0).getCountryCode();
                        String postalCode = addresses.get(0).getPostalCode();
                        deliveryAddress.setText(address + ", " + postalCode + ", " + city + ", " + country);
                    } else {
                        Toast.makeText(getContext(), "Your address isn't found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Your gps is disabled", Toast.LENGTH_SHORT).show();
                }

            }
        });
        CompoundButton.OnCheckedChangeListener filterChipListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Chip chip = (Chip) buttonView;

                if (isChecked) {
                    chosen.add(chip.getText().toString().toLowerCase());
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                } else {
                    chosen.removeIf(c -> c.equals(chip.getText().toString().toLowerCase()));
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                }
                applyFilters(minOrderCost, freeDelivery);
            }
        };
        ConsumerDatabase.getInstance().getCategories(set -> {
            set.forEach(n -> {
                Chip chip = new Chip(view.getContext());
                chip.setText(n);
                chipMap.put(n, chip);
                chip.setChipBackgroundColorResource(R.color.colorWhite);
                chip.setChipStrokeWidth((float) 0.1);
                chip.setChipStrokeColorResource(R.color.colorPrimary);
                chip.setCheckable(true);
                chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                chip.setRippleColorResource(R.color.colorPrimaryDark);
                chip.setCheckedIconVisible(false);
                chip.setChipIconTintResource(R.color.colorPrimary);
                chip.setOnCheckedChangeListener(filterChipListener);
                chipGroup.addView(chip);
            });
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        catFragment = new CategoriesFragment();
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.childfrag_container, catFragment);
        ft.commit();
    }

    public void applyFilters(boolean m, boolean d) {
        restaurantsFragment = new RestaurantsFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("categories", new ArrayList<>(chosen));
        bundle.putString("address", address);
        bundle.putBoolean("minOrderCost", m);
        bundle.putBoolean("freeDelivery", d);
        restaurantsFragment.setArguments(bundle);
        ft = fm.beginTransaction();
        ft.addToBackStack(RestaurantsFragment.RESTAURANT_FRAGMENT_TAG);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.childfrag_container, restaurantsFragment);
        ft.commit();
    }

    @Override
    public void openCategory(List<String> chosenList, String address, boolean m, boolean d) {
        // if present, close the RestaurantsFragment
        Fragment fOpen = fm.findFragmentByTag(RestaurantsFragment.RESTAURANT_FRAGMENT_TAG);
        if (fOpen != null) {
            Log.i("MADAPP", "Closing restaurantsFragment..");
            fm.beginTransaction().remove(fOpen).commit();
        }
        if(chosenList != null) {
            for (String s : chosenList) {
                Chip selectedChip = chipMap.get(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
                if (selectedChip != null) {
                    selectedChip.setChecked(true);
                }
            }
        }
        filter.setVisibility(View.VISIBLE);
        restaurantsFragment = new RestaurantsFragment();
        Bundle bundle = new Bundle();
        this.address = address;
        bundle.putStringArrayList("categories", (ArrayList<String>) chosenList);
        bundle.putString("address", address);
        bundle.putBoolean("minOrderCost", m);
        bundle.putBoolean("freeDelivery", d);
        restaurantsFragment.setArguments(bundle);
        ft = fm.beginTransaction();
        ft.addToBackStack(RestaurantsFragment.RESTAURANT_FRAGMENT_TAG);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.childfrag_container, restaurantsFragment);
        ft.commit();
    }



    @Override
    public boolean getDeliveryCostParam() {
        return freeDelivery;
    }

    @Override
    public boolean getMinOrderCostParam() {
        return minOrderCost;
    }

    @Override
    public void closeFilters() {
        filter.setVisibility(View.GONE);
    }
}
