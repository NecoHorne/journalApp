package com.necohorne.alcjournalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.necohorne.alcjournalapp.Data.JournalDbHelper;
import com.necohorne.alcjournalapp.Fragments.ExitWithoutSaveFragment;
import com.necohorne.alcjournalapp.Models.JournalEntry;
import com.necohorne.alcjournalapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.text.TextUtils.isEmpty;

public class AddEntryActivity extends AppCompatActivity {

    private String TAG = "AddEntryActivity";
    private EditText titleText;
    private EditText entryBody;

    private boolean saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        initViews();
        saveState = false;
    }

    @Override
    public void onBackPressed() {
        if(saveState){
            super.onBackPressed();
        } else {
            ExitWithoutSaveFragment exitWithoutSaveFragment = new ExitWithoutSaveFragment();
            exitWithoutSaveFragment.show(getFragmentManager(), "dialog_exit_without_save" );
        }
    }

    private void initViews(){
        Button saveButton = findViewById(R.id.button_entry_save);
        titleText = findViewById(R.id.edit_text_entry_title);
        entryBody = findViewById(R.id.edit_text_entry_body);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(titleText.getText().toString()) && !isEmpty(entryBody.getText().toString())){
                    saveJournalEntry();
                } else {
                    Toast.makeText(AddEntryActivity.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveJournalEntry(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        JournalDbHelper dbHelper = new JournalDbHelper(AddEntryActivity.this);

        if(user != null){
            //get details from editText views.
            String titleString = titleText.getText().toString();
            String entryText = entryBody.getText().toString();
            //get User ID from firebase User Object
            String userId = user.getUid();
            //create a journal entry instance
            JournalEntry journalEntry = new JournalEntry(titleString, entryText, getTimeStamp(), userId);

            //add journal entry to database
            dbHelper.addEntry(journalEntry);

            //set the save state to true, let user know item was saved, go back to main activity and finish this activity.
            saveState = true;
            Toast.makeText(AddEntryActivity.this, "Entry Saved!" , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddEntryActivity.this, MainActivity.class));
            finish();
        }
    }

    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date());
    }

}
