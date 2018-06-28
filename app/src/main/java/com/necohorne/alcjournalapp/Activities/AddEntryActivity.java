package com.necohorne.alcjournalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    private EditText titleText;
    private EditText entryBody;

    private boolean saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        initViews();
        checkAuthenticationState();
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

    //-----MENU-----//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.add_entry_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.add_menu_save:
                if(!isEmpty(titleText.getText().toString()) && !isEmpty(entryBody.getText().toString())){
                    saveJournalEntry();
                } else {
                    Toast.makeText(AddEntryActivity.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    private void checkAuthenticationState() {
        //this method checks if the current user is signed in and authenticated via the firebase login.
        //If user is not signed in, activity will finish and take user back to sign in.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent mLogOutIntent = new Intent( AddEntryActivity.this, SignInActivity.class );
            mLogOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity( mLogOutIntent );
            finish();
        }
    }

    private void initViews(){
        titleText = findViewById(R.id.edit_text_entry_title);
        entryBody = findViewById(R.id.edit_text_entry_body);
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
