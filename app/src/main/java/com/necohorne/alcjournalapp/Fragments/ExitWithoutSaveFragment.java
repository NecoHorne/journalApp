package com.necohorne.alcjournalapp.Fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necohorne.alcjournalapp.R;


public class ExitWithoutSaveFragment extends DialogFragment {

//    private final String TAG = "DeleteEntryFragment";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_save_entry, container, false);

        TextView cancel = view.findViewById(R.id.text_cancel_exit);
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        } );
        TextView delete = view.findViewById(R.id.text_confirm_exit);
        delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                getActivity().finish();
            }
        } );

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        return view;
    }

}
