package com.example.stickynotes.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDB extends RoomDatabase {
    public abstract Dao getNoteDao();

    private static NoteDB noteDB;

//    public static NoteDB getInstance(Context context) {
//        if (null == noteDB) {
//            noteDB = buildDatabaseInstance(context);
//        }
//        return noteDB;
//    }

//    private static NoteDB buildDatabaseInstance(Context context) {
//        return Room.databaseBuilder(context,
//                NoteDB.class,
//               "noteDB")
//                .allowMainThreadQueries().build();
//    }

    private static volatile NoteDB INSTANCE;
    private static final String DATABASE_NAME = "note_DB";

    public static NoteDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder
                            (context.getApplicationContext(), NoteDB.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public void cleanUp() {
        noteDB = null;
    }

}
