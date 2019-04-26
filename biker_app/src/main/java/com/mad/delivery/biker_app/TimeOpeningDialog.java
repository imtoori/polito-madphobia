package com.mad.delivery.biker_app;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.mad.delivery.biker_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TimeOpeningDialog extends DialogFragment {
    OrderStatus currentStatus;
    ListDialogListener listener;
    String[] list;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        builder.setTitle(R.string.changing_status);
        builder.setView(R.layout.timerange_layout);
        return builder.create();
    }

    public interface ListDialogListener {
        void onElementChoosen(OrderStatus s);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            listener = (ListDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
