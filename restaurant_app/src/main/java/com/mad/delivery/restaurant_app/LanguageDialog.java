package com.mad.delivery.restaurant_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        builder.setTitle(R.string.choose_language)
                .setSingleChoiceItems(selectedItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Context context;
                context=LanguageHelper.changeLocale(getContext(), getResources(), "en");
                onAttach(context);

            }
        });

        return builder.create();
    }


    public interface ListDialogListener {
        void onElementChoosen(OrderStatus s);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

    }
}