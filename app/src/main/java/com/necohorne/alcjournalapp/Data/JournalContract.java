package com.necohorne.alcjournalapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class JournalContract {

    public static final String AUTHORITY = "com.necohorne.alcjournalapp.journalentry";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_JOURNAL = "journal";

    public static final class EntryJournal implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_JOURNAL).build();

        // Task table and column names
        public static final String TABLE_NAME = "journal";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ENTRY_BODY = "body";
        public static final String COLUMN_ENTRY_DATE = "date";
        public static final String COLUMN_ENTRY_UID = "user";
    }
}
