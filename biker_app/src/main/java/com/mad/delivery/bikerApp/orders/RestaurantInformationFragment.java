package com.mad.delivery.bikerApp.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Restaurant;

public class RestaurantInformationFragment extends Fragment {
        private RestaurantInformationFragment.OnRestaurantInformationFragmentInteraction mListener;
        private TextView fullName;
        private TextView phoneNumber;
        private TextView email;
        private TextView deliveryAddress;
        private TextView description;
        private Restaurant u;

        public RestaurantInformationFragment() {
            // Required empty public constructor
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_user_information, container, false);
            fullName = view.findViewById(R.id.main_name);
            phoneNumber = view.findViewById(R.id.mainprofile_phone);
            email = view.findViewById(R.id.main_email);
            deliveryAddress = view.findViewById(R.id.main_road);
            description = view.findViewById(R.id.main_description);

            u = getArguments().getParcelable("restaurant");
            fullName.setText(u.previewInfo.name);
            phoneNumber.setText(u.phoneNumber);
            email.setText(u.email);
            deliveryAddress.setText(u.road + " " + u.houseNumber + ", " + u.postCode + " " + u.city + "(door " + u.doorPhone+ ")");
            description.setText(u.previewInfo.description);

            return view;
        }

        public interface OnRestaurantInformationFragmentInteraction {
            public Order getOrder();
        }

    }
