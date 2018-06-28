package com.necohorne.alcjournalapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.necohorne.alcjournalapp.Data.JournalDbHelper;
import com.necohorne.alcjournalapp.Models.JournalEntry;
import com.necohorne.alcjournalapp.R;
import com.necohorne.alcjournalapp.Utilities.JournalEntryRecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private String TAG = "MainActivity";
    private Intent mLogOutIntent;
    private JournalEntryRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogOutIntent = new Intent( MainActivity.this, SignInActivity.class );
        checkAuthenticationState();
        new GetDb().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetDb().execute();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRecycler(ArrayList<JournalEntry> entries){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        LinearLayoutManager journalLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(journalLayoutManager);
        mAdapter = new JournalEntryRecyclerAdapter(MainActivity.this, entries);
        recyclerView.setAdapter(mAdapter);
    }

    private void checkAuthenticationState() {
        //this method checks if the current user is signed in and authenticated via the firebase login.
        //If user is not signed in, activity will finish and take user back to sign in.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            mLogOutIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity( mLogOutIntent );
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_logout:
                logOut();
                break;
            case R.id.menu_main_add_entry:
                startActivity(new Intent(MainActivity.this, AddEntryActivity.class));
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    private void logOut() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText( MainActivity.this, "Successfully logged out", Toast.LENGTH_LONG ).show();
            startActivity( mLogOutIntent );
            finish();
        }
    }

    private class GetDb extends AsyncTask<Void, Void, ArrayList<JournalEntry>> {

        @Override
        protected ArrayList<JournalEntry> doInBackground(Void... voids) {
            JournalDbHelper dbHelper = new JournalDbHelper(MainActivity.this);
            return dbHelper.getAllEntries();
        }

        @Override
        protected void onPostExecute(ArrayList<JournalEntry> journalEntries) {
            initRecycler(journalEntries);
        }
    }

}
