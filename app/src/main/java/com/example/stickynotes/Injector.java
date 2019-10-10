package com.example.stickynotes;

import android.content.Context;

import com.example.stickynotes.DB.NoteDB;
import com.example.stickynotes.DB.NoteRepository;

public class Injector {
    public static NoteRepository provideTaskRepository(Context context) {
        NoteDB database = NoteDB.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return NoteRepository.getInstance(database.getNoteDao(), executors);
    }

    public static AddNoteViewModelFactory provideDetailViewModelFactory(Context context) {
        NoteRepository repository = provideTaskRepository(context);
        return new AddNoteViewModelFactory(repository);
    }
}
