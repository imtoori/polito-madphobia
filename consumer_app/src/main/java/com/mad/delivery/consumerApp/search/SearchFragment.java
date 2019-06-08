package com.mad.delivery.consumerApp.search;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.GPSTracker;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.RestaurantCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements CategoriesFragment.OnCategorySelected, OnFilterChanged {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ImageButton search;
    private ImageButton location;
    private CategoriesFragment catFragment;
    private RestaurantsFragment restaurantsFragment;
    private CardView filter;
    private Chip delivery, minorder, review;
    private String address = "";
    private TextView deliveryAddress;
    private Set<String> chosen;
    private boolean freeDelivery, minOrderCost, reviewFlag;
    private Double latitude;
    private Double longitude;
    private RecyclerView chipRecyclerView;
    private ChipFilterAdapter chipFilterAdapter;
    private List<RestaurantCategory> chips;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

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
        review = view.findViewById(R.id.order_review);
        location = view.findViewById(R.id.location_img);
        deliveryAddress = view.findViewById(R.id.delivery_address_et);

        deliveryAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chipRecyclerView = view.findViewById(R.id.chip_recycler_view);
        chipRecyclerView.hasFixedSize();
        chipRecyclerView.setLayoutManager(horizontalLayoutManager);
        chips = new ArrayList<>();
        chipFilterAdapter = new ChipFilterAdapter(chips, this);
        chipRecyclerView.setAdapter(chipFilterAdapter);
        chosen = new HashSet<>();
        freeDelivery = false;
        minOrderCost = false;
        reviewFlag = false;

        delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                applyFilters(minOrderCost, b, reviewFlag);
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
                applyFilters(b, freeDelivery, reviewFlag);
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
        review.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                applyFilters(minOrderCost, freeDelivery, b);
                if (b) {
                    reviewFlag = true;
                    review.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    review.setTextColor(getResources().getColor(R.color.colorWhite, null));
                    review.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                } else {
                    reviewFlag = false;
                    review.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
                    review.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    review.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
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
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

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
        ConsumerDatabase.getInstance().getAllRestaurantCategories(list -> {
            chips.clear();
            chipFilterAdapter.notifyDataSetChanged();
            chips.addAll(list);
            chipFilterAdapter.notifyDataSetChanged();
        });
    }

    public void applyFilters(boolean m, boolean d, boolean r) {
        restaurantsFragment = new RestaurantsFragment();
        Bundle bundle = new Bundle();
        Log.d("MADAPP", chosen.toString());
        bundle.putStringArrayList("categories", new ArrayList<>(chosen));
        bundle.putString("address", address);
        bundle.putBoolean("minOrderCost", m);
        bundle.putBoolean("freeDelivery", d);
        bundle.putBoolean("orderByReviews", r);

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
        Log.d("MADAPP", "opening Category Fragment");
        Fragment fOpen = fm.findFragmentByTag(RestaurantsFragment.RESTAURANT_FRAGMENT_TAG);
        if (fOpen != null) {
            Log.d("MADAPP", "opening Category Fragment: Already present. Removing..");
            fm.beginTransaction().remove(fOpen).commit();
        }
        if (chosenList != null) {
            for (String s : chosenList) {
                chips.stream().forEach(item -> {
                    Log.d("MADAPP", "chosen=" + s + ", but item:" + item.name);
                    if (item.name.equalsIgnoreCase(s)) item.selected = true;
                    chosen.add(s);
                });
                chipFilterAdapter.notifyDataSetChanged();
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
        bundle.putBoolean("orderByReviews", reviewFlag);
        if (deliveryAddress.getText().toString().length() == 0) {
            longitude = null;
            latitude = null;
        }
        if (latitude != null)
            bundle.putDouble("latitude", latitude);
        if (longitude != null)
            bundle.putDouble("longitude", longitude);
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

    @Override
    public void changed(Chip chip, boolean checked) {
        if (checked) {
            chosen.add(chip.getText().toString().toLowerCase());
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark, null)));
            chip.setTextColor(getResources().getColor(R.color.colorWhite, null));
            chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));

        } else {
            chosen.removeIf(c -> c.equals(chip.getText().toString().toLowerCase()));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite, null)));
            chip.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark, null)));
        }
        applyFilters(minOrderCost, freeDelivery, reviewFlag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("PLACES", "Place: " + place.getName() + ", " + place.getId() + place.getLatLng().toString());
                deliveryAddress.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("PLACES", status.getStatusMessage());
            }
        }
    }
}
