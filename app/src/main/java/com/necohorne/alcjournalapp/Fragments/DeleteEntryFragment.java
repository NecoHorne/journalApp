package com.necohorne.alcjournalapp.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.necohorne.alcjournalapp.Constants.Constants;
import com.necohorne.alcjournalapp.Data.JournalDbHelper;
import com.necohorne.alcjournalapp.R;


public class DeleteEntryFragment extends DialogFragment {

    private final String TAG = "DeleteEntryFragment";
    private View mView;
    private Context mContext;

    private int mPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        mPosition = getArguments().getInt(Constants.DATABASE_POSITION);
        if (mPosition >= 0){
            Log.d( TAG, "Key = " + mPosition);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate( R.layout.dialog_delete_entry, container, false);
        mContext = getActivity();

        TextView cancel = mView.findViewById(R.id.cancel);
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        } );
        TextView delete = mView.findViewById(R.id.confirm_delete);
        delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JournalDbHelper dbHelper = new JournalDbHelper(mContext);
                dbHelper.deleteEntry(mPosition);
                Toast.makeText(mContext, "Entry Deleted", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                getActivity().finish();
            }
        } );

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        return mView;
    }

}
