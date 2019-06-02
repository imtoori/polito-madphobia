package com.mad.delivery.consumerApp.search.Order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.ConsumerDatabase;
import com.mad.delivery.consumerApp.HomeActivity;
import com.mad.delivery.consumerApp.R;
import com.mad.delivery.consumerApp.search.ReviewsAdapter;
import com.mad.delivery.resources.Feedback;
import com.mad.delivery.resources.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompletingOrderDialogFragment extends DialogFragment {
    private Context context;
    private Order order;
    private Button btnClose;
    private ImageView imgDone, imgErr;
    private ProgressBar pgBar;
    private TextView desc;

    public static CompletingOrderDialogFragment newInstance(Order order) {
        CompletingOrderDialogFragment sendingFragment = new CompletingOrderDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("order", order);
        sendingFragment.setArguments(args);
        return sendingFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_completing_order, container, false);
        try {
            this.order = (Order) getArguments().get("order");
        } catch(NullPointerException e) {
            dismiss();
        }
        btnClose = rootView.findViewById(R.id.btn_close_dialog);
        imgDone = rootView.findViewById(R.id.img_done);
        pgBar = rootView.findViewById(R.id.progressBar);
        desc = rootView.findViewById(R.id.tv_desc_activity);
        imgErr = rootView.findViewById(R.id.img_error);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ConsumerDatabase.getInstance().putOrder(order, context, success -> {
                btnClose.setVisibility(View.VISIBLE);
                pgBar.setVisibility(View.GONE);
                if(success) {
                    imgDone.setVisibility(View.VISIBLE);
                    imgErr.setVisibility(View.GONE);
                    desc.setTextColor(getResources().getColor(R.color.colorGreen, null));
                    desc.setText("Your order has been sent.");
                    btnClose.setText("Go to Home");
                    btnClose.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    });
                } else {
                    imgErr.setVisibility(View.VISIBLE);
                    imgDone.setVisibility(View.GONE);
                    desc.setTextColor(getResources().getColor(R.color.colorRed, null));
                    desc.setText("Your order contains one or more items which are not available anymore. Please try with different items.");
                    btnClose.setText("I got it");
                    btnClose.setOnClickListener(v -> {
                        dismiss();
                    });
                }
            });
        } catch (IOException e) {
            btnClose.setVisibility(View.VISIBLE);
            pgBar.setVisibility(View.GONE);
            imgErr.setVisibility(View.VISIBLE);
            imgDone.setVisibility(View.GONE);
            desc.setTextColor(getResources().getColor(R.color.colorRed, null));
            desc.setText("A fatal error occurred. Contact the administrator.");
        }
    }
}