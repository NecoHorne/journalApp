package com.necohorne.alcjournalapp.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necohorne.alcjournalapp.Activities.EntryDetailActivity;
import com.necohorne.alcjournalapp.Constants.Constants;
import com.necohorne.alcjournalapp.Models.JournalEntry;
import com.necohorne.alcjournalapp.R;

import java.util.ArrayList;

public class JournalEntryRecyclerAdapter extends RecyclerView.Adapter<JournalEntryRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<JournalEntry> mJournalEntries;

    public JournalEntryRecyclerAdapter(Context context, ArrayList<JournalEntry> journalEntries) {
        mContext = context;
        mJournalEntries = journalEntries;
    }

    @NonNull
    @Override
    public JournalEntryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.journal_entry_list_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalEntryRecyclerAdapter.ViewHolder holder, int position) {

        final JournalEntry journalEntry = mJournalEntries.get(position);

        holder.entryTitle.setText(journalEntry.getTitle());
        holder.entryDate.setText(journalEntry.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(journalEntry);
            }
        });
    }

    private void showDetail(JournalEntry journalEntry) {
        Intent detailIntent = new Intent(mContext, EntryDetailActivity.class);
        detailIntent.putExtra(Constants.DATABASE_POSITION, journalEntry.getDataBasePos());
        mContext.startActivity(detailIntent);
    }

    @Override
    public int getItemCount() {
        return mJournalEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView entryTitle;
        public TextView entryDate;

        public ViewHolder(View itemView) {
            super(itemView);
            entryTitle = itemView.findViewById(R.id.text_title);
            entryDate = itemView.findViewById(R.id.text_date);
        }
    }
}
