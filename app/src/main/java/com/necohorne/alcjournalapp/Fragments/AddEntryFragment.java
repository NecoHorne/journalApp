package com.necohorne.alcjournalapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.necohorne.alcjournalapp.Models.JournalEntry;
import com.necohorne.alcjournalapp.R;

import java.util.Calendar;
import java.util.Date;

import static android.text.TextUtils.isEmpty;


public class AddEntryFragment extends DialogFragment {

    //-----Views-----//
    private View mView;
    private EditText titleText;
    private EditText entryBody;

    private boolean saveState;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        saveState = false;
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_add_entry, container, false);
        initViews();
        return mView;
    }

    private void initViews(){
        Button saveButton = mView.findViewById(R.id.button_entry_save);
        titleText = mView.findViewById(R.id.edit_text_entry_title);
        entryBody = mView.findViewById(R.id.edit_text_entry_body);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(titleText.getText().toString()) && !isEmpty(entryBody.getText().toString())){
                    saveJournalEntry();
                } else {
                    Toast.makeText(mContext, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveJournalEntry(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            //get details from editText views.
            String titleString = titleText.getText().toString();
            String entryText = entryBody.getText().toString();
            //get User ID from firebase User Object
            String userId = user.getUid();
            //Get date and time for entry
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            String addDate = today.toString();
            //create a journal entry instance
            JournalEntry journalEntry = new JournalEntry(titleString, entryText, addDate, userId);

            //TODO add journal entry to database

            //set the save state to true
            saveState = true;

            //dismiss dialog fragment.
            getDialog().dismiss();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(saveState){
            super.onDismiss(dialog);
        } else {
            //TODO code for when not saved.
        }
    }
}
