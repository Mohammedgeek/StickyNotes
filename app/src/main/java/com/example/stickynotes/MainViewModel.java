package com.example.stickynotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteDB;
import com.example.stickynotes.DB.NoteRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private NoteDB appDatabase;
    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> mAllNotes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        appDatabase = NoteDB.getInstance(application.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        mNoteRepository = NoteRepository.getInstance(appDatabase.getNoteDao(), executors);
        mAllNotes = mNoteRepository.getAll();
    }


    public LiveData<List<Note>> getAllTasks() {
        return mAllNotes;
    }

    public void deleteTask(Note noteEntity) {
        mNoteRepository.deleteNote(noteEntity);
    }

    public void deleteAllTasks() {
        mNoteRepository.deleteAllTasks();
    }
}
