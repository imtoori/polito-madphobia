package com.mad.delivery.bikerApp.orders;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mad.delivery.bikerApp.R;

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
