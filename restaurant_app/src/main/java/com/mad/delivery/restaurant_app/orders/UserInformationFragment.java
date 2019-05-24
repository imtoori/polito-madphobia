package com.mad.delivery.restaurant_app.orders;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.delivery.resources.Customer;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.User;
import com.mad.delivery.restaurant_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {
    private OnUserInformationFragmentInteraction mListener;
    private TextView fullName;
    private TextView phoneNumber;
    private TextView email;
    private TextView deliveryAddress;
    private TextView description;
    private User u;
    private String delivery;

    public UserInformationFragment() {
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

        u = getArguments().getParcelable("client");
        delivery = (String) getArguments().get("delivery");
        fullName.setText(u.name + " " + u.lastName);
        phoneNumber.setText(u.phoneNumber);
        email.setText(u.email);
        deliveryAddress.setText(delivery);
        description.setText(u.description);

        return view;
    }

    public interface OnUserInformationFragmentInteraction {
        public Order getOrder();
    }

}
