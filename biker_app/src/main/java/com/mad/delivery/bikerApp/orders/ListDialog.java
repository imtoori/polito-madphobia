package com.mad.delivery.bikerApp.orders;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mad.delivery.bikerApp.R;

public class ListDialog extends DialogFragment {
    OrderStatus currentStatus;
    ListDialogListener listener;
    String[] list;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        try {

            currentStatus = (OrderStatus) getArguments().getSerializable("currentStatus");

        } catch(NullPointerException e) {
            currentStatus = OrderStatus.pending;
        }
        List<String> stringList = getStatusAsList(currentStatus);
        list = new String[stringList.size()];
        list = stringList.toArray(list);

        builder.setTitle(R.string.changing_status)
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("MADAPP", "clicked here and choosen");
                        listener.onElementChoosen(OrderStatus.valueOf(list[i]));
                    }
                });
        return builder.create();
    }

    public static List<String> getStatusAsList(OrderStatus initial) {
        List<String> list = new ArrayList<>();
        switch(initial) {
            case pending:
                list.addAll(Arrays.asList(OrderStatus.preparing.toString(), OrderStatus.ready.toString(), OrderStatus.completed.toString(), OrderStatus.canceled.toString()));
                break;
            case preparing:
                list.addAll(Arrays.asList(OrderStatus.ready.toString(), OrderStatus.completed.toString(), OrderStatus.canceled.toString()));
                break;
            case ready:
                list.addAll(Arrays.asList(OrderStatus.completed.toString()));
                break;
            case completed:
                list.add(OrderStatus.completed.toString());
                break;
            case canceled:
                list.add(OrderStatus.pending.toString());
                break;
            default:
                list.addAll(Arrays.asList(OrderStatus.pending.toString(), OrderStatus.preparing.toString(), OrderStatus.ready.toString(), OrderStatus.completed.toString(), OrderStatus.canceled.toString()));
        }
        return list;
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