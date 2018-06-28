package com.necohorne.alcjournalapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.necohorne.alcjournalapp.Constants.Constants;
import com.necohorne.alcjournalapp.Data.JournalDbHelper;
import com.necohorne.alcjournalapp.Fragments.DeleteEntryFragment;
import com.necohorne.alcjournalapp.Models.JournalEntry;
import com.necohorne.alcjournalapp.R;

import static android.text.TextUtils.isEmpty;

public class EntryDetailActivity extends AppCompatActivity {

    private JournalEntry mJournalEntry;
    private TextView title;
    private TextView entryBody;
    private TextView date;
    private EditText editTitle;
    private EditText editBody;
    private Button editSave;
    private boolean isEditing;
    private String mNewTitle;
    private String mNewBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        checkAuthenticationState();
        initUi();
        new GetDb().execute();
        isEditing = false;
    }

    @Override
    public void onBackPressed() {
        if(!isEditing){
            super.onBackPressed();
        }else {
            exitEdit();
        }
    }

    private void initUi() {
        title = findViewById(R.id.text_detail_title);
        entryBody = findViewById(R.id.text_detail_entry);
        date = findViewById(R.id.text_detail_date);
    }

    private void editMode(){
        isEditing = true;
        editTitle = findViewById(R.id.edit_text_edit_title);
        editTitle.setVisibility(View.VISIBLE);
        editTitle.setText(mJournalEntry.getTitle());
        editBody = findViewById(R.id.edit_text_edit_entry);

        editBody.setVisibility(View.VISIBLE);
        editBody.setText(mJournalEntry.getBody());

        title.setVisibility(View.GONE);
        entryBody.setVisibility(View.GONE);

        editSave = findViewById(R.id.button_edit_save);
        editSave.setVisibility(View.VISIBLE);

        if(!isEmpty(editTitle.getText()) && !isEmpty(editBody.getText())){
            editSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNewTitle = editTitle.getText().toString();
                    mNewBody = editBody.getText().toString();
                    new UpdateDb().execute();
                    exitEdit();
                }
            });
        }
    }

    private void exitEdit(){
        isEditing = false;
        editTitle.setVisibility(View.GONE);
        editBody.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        entryBody.setVisibility(View.VISIBLE);
        editSave.setVisibility(View.GONE);
    }

    private void deleteEntry(JournalEntry journalEntry) {
        int keyRef = journalEntry.getDataBasePos();
        DeleteEntryFragment deleteEntryFragment = new DeleteEntryFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.DATABASE_POSITION, keyRef);
        deleteEntryFragment.setArguments(args);
        deleteEntryFragment.show(getFragmentManager(), "dialog_delete_entry" );
    }

    private void checkAuthenticationState() {
        //this method checks if the current user is signed in and authenticated via the firebase login.
        //If user is not signed in, activity will finish and take user back to sign in.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent mLogOutIntent = new Intent( EntryDetailActivity.this, SignInActivity.class );
            mLogOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity( mLogOutIntent );
            finish();
        }
    }

    //-----MENU-----//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.entry_detail_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.detail_menu_edit:
                editMode();
                break;
            case R.id.detail_menu_delete:
                deleteEntry(mJournalEntry);
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    //-----Background Tasks-----//
    private class GetDb extends AsyncTask<Void, Void, JournalEntry> {

        @Override
        protected JournalEntry doInBackground(Void... voids) {
            JournalDbHelper dbHelper = new JournalDbHelper(EntryDetailActivity.this);
            Intent intent = getIntent();
            int pos = intent.getIntExtra(Constants.DATABASE_POSITION, 0);
            return dbHelper.getEntry(pos);
        }

        @Override
        protected void onPostExecute(JournalEntry journalEntry) {
            mJournalEntry = journalEntry;
            title.setText(mJournalEntry.getTitle());
            entryBody.setText(mJournalEntry.getBody());
            date.setText(mJournalEntry.getDate());
        }
    }

    private class UpdateDb extends AsyncTask<Void , Void, JournalEntry> {

        @Override
        protected JournalEntry doInBackground(Void... voids) {
            JournalDbHelper dbHelper = new JournalDbHelper(EntryDetailActivity.this);
            mJournalEntry.setTitle(mNewTitle);
            mJournalEntry.setBody(mNewBody);
            dbHelper.updateEntry(mJournalEntry);
            return mJournalEntry;
        }

        @Override
        protected void onPostExecute(JournalEntry journalEntry) {
            mJournalEntry = journalEntry;
            title.setText(mJournalEntry.getTitle());
            entryBody.setText(mJournalEntry.getBody());
            date.setText(mJournalEntry.getDate());
        }
    }
}
