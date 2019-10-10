package com.example.stickynotes.DB;

import androidx.lifecycle.LiveData;

import com.example.stickynotes.AppExecutors;

import java.util.List;

public class NoteRepository {
    private static final Object LOCK = new Object();
    private static NoteRepository sInstance;
    private Dao mNoteDao;
    private AppExecutors mExecutors;

    private NoteRepository(Dao noteDao, AppExecutors executors) {
        mNoteDao = noteDao;
        mExecutors = executors;
    }

    public synchronized static NoteRepository getInstance(Dao taskDao, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NoteRepository(taskDao, executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<Note>> getAll() {
        return mNoteDao.getAll();
    }


    public void addNote(final Note note) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.insert(note);
            }
        });
    }

    public void updateNote(final Note note) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.update(note);
            }
        });
    }

    public void deleteNote(final Note note) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.delete(note);
            }
        });
    }

    public void deleteAllTasks() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteAll();
            }
        });
    }
}
