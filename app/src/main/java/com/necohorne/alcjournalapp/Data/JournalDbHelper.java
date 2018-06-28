package com.necohorne.alcjournalapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.necohorne.alcjournalapp.Models.JournalEntry;

import java.util.ArrayList;

public class JournalDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "journalDb.db";
    private static final int VERSION = 1;

    public JournalDbHelper(Context context ) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + JournalContract.EntryJournal.TABLE_NAME + " (" +
                JournalContract.EntryJournal._ID + " INTEGER PRIMARY KEY, " +
                JournalContract.EntryJournal.COLUMN_TITLE + " TEXT NOT NULL, " +
                JournalContract.EntryJournal.COLUMN_ENTRY_BODY + " TEXT NOT NULL, " +
                JournalContract.EntryJournal.COLUMN_ENTRY_DATE + " TEXT NOT NULL, " +
                JournalContract.EntryJournal.COLUMN_ENTRY_UID + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + JournalContract.EntryJournal.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD Operations
     */

    public void addEntry(JournalEntry entry) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put( JournalContract.EntryJournal.COLUMN_TITLE, entry.getTitle());
        value.put( JournalContract.EntryJournal.COLUMN_ENTRY_BODY, entry.getBody());
        value.put( JournalContract.EntryJournal.COLUMN_ENTRY_DATE, entry.getDate());
        value.put( JournalContract.EntryJournal.COLUMN_ENTRY_UID, entry.getUserId());

        //Insert to a row
        db.insert(JournalContract.EntryJournal.TABLE_NAME, null,value );
        db.close();
    }

    public JournalEntry getEntry(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(JournalContract.EntryJournal.TABLE_NAME,
                new String[] {JournalContract.EntryJournal._ID,
                        JournalContract.EntryJournal.COLUMN_TITLE,
                        JournalContract.EntryJournal.COLUMN_ENTRY_BODY,
                        JournalContract.EntryJournal.COLUMN_ENTRY_DATE,
                        JournalContract.EntryJournal.COLUMN_ENTRY_UID}, JournalContract.EntryJournal._ID + "=?",
                new String[] {String.valueOf(id)}, null, null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        JournalEntry journalEntry = new JournalEntry(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4 ));
        journalEntry.setDataBasePos(cursor.getInt(0));

        cursor.close();
        db.close();
        return journalEntry;
    }

    public ArrayList<JournalEntry> getAllEntries() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<JournalEntry> entryArrayListList = new ArrayList<>();

        //Select all contacts

        String selectAll = "SELECT * FROM " + JournalContract.EntryJournal.TABLE_NAME;
        Cursor cursor = db.rawQuery( selectAll,null );

        //Loop through

        if (cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                String date = cursor.getString(3);
                String uId = cursor.getString(4);


                JournalEntry journalEntry = new JournalEntry(title, body, date, uId);
                journalEntry.setDataBasePos(position);

                //add journalEntry to journalEntry list
                entryArrayListList.add(journalEntry);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entryArrayListList;

    }

    public int updateEntry(JournalEntry journalEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JournalContract.EntryJournal.COLUMN_TITLE, journalEntry.getTitle());
        values.put(JournalContract.EntryJournal.COLUMN_ENTRY_BODY, journalEntry.getBody());
        values.put(JournalContract.EntryJournal.COLUMN_ENTRY_DATE, journalEntry.getDate());
        values.put(JournalContract.EntryJournal.COLUMN_ENTRY_UID, journalEntry.getUserId());

        return db.update( JournalContract.EntryJournal.TABLE_NAME,
                values,
                JournalContract.EntryJournal._ID + "=?",
                new String[]{String.valueOf(journalEntry.getDataBasePos())});
    }

    public void deleteEntry(int dataBasePos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( JournalContract.EntryJournal.TABLE_NAME, JournalContract.EntryJournal._ID + "=?",
                new String[]{String.valueOf(dataBasePos)});
        db.close();
    }
}
