package com.gypsophila.androidpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

/**
 * Created by Gypsophila on 2016/6/29.
 */
public class TimePickerFragment extends DialogFragment {
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_time, container, false);
//        TimePicker timePicker = new TimePicker(getActivity());
//        return view;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.dialog_time_timePicker);
        timePicker.setIs24HourView(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null).create();
    }
}
