package com.mad.delivery.restaurant_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LanguageDialog extends DialogFragment {
    int selected = 0;
    String[] selectedItems;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new String[2];  // Where we track the selected items
        selectedItems[0] = "Italiano";
        selectedItems[1] = "English";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.completing_order_title_dialog)
                .setSingleChoiceItems(selectedItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LanguageHelper.changeLocale(getContext(), getResources(), "it");
            }
        });

        return builder.create();
    }


    public interface ListDialogListener {
        void onElementChoosen(OrderStatus s);
    }

}