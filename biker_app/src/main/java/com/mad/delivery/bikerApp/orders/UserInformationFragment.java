package com.mad.delivery.bikerApp.orders;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;
import com.mad.delivery.resources.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {
    private OnUserInformationFragmentInteraction mListener;
    private TextView fullName;
    private TextView phoneNumber;
    private TextView email;
    private TextView deliveryAddress;
    private String address;
    private User u;

    // for restaurant
    private TextView restaurantName;
    private TextView restaurantPhoneNumber;
    private TextView restaurantEmail;
    private TextView restaurantAddress;
    private Restaurant restaurant;

    public UserInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);

        // for customer
        fullName = view.findViewById(R.id.main_name);
        phoneNumber = view.findViewById(R.id.mainprofile_phone);
        email = view.findViewById(R.id.main_email);
        deliveryAddress = view.findViewById(R.id.main_road);
        // for restaurant
        restaurantName = view.findViewById(R.id.restaurant_name);
        restaurantPhoneNumber = view.findViewById(R.id.restaurantprofile_phone);
        restaurantEmail = view.findViewById(R.id.restaurant_email);
        restaurantAddress = view.findViewById(R.id.restaurant_road);

        u = getArguments().getParcelable("client");
        address = getArguments().getString("address");
        restaurant = getArguments().getParcelable("restaurant");

        // for customer
        fullName.setText(u.name + " " + u.lastName);
        phoneNumber.setText(u.phoneNumber);
        email.setText(u.email);
        deliveryAddress.setText(address);

        // for restaurant
        restaurantName.setText(restaurant.previewInfo.name);
        restaurantPhoneNumber.setText(restaurant.phoneNumber);
        restaurantEmail.setText(restaurant.email);
        restaurantAddress.setText(restaurant.road + " " + restaurant.houseNumber + ", " + restaurant.postCode + " " + restaurant.city);

        return view;
    }

    public interface OnUserInformationFragmentInteraction {
        public Order getOrder();
    }

}
