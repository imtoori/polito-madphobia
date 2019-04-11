package com.mad.delivery.restaurant_app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
    private Customer u;

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
        fullName.setText(u.name + " " + u.lastName);
        phoneNumber.setText(u.phoneNumber);
        email.setText(u.email);
        deliveryAddress.setText(u.road + " " + u.houseNumber + ", " + u.postCode + " " + u.city + "(door " + u.doorPhone+ ")");
        description.setText(u.description);

        return view;
    }

    public interface OnUserInformationFragmentInteraction {
        public Order getOrder();
    }

}
