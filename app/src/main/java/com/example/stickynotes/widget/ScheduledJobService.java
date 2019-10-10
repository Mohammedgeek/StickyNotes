package com.example.stickynotes.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteDB;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

public class ScheduledJobService extends JobService {
    private AsyncTask mBackgroundTask;
    private static List<Note> noteEntities;

    @SuppressWarnings("unchecked")
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {

        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ScheduledJobService.this;
                NoteDB db = NoteDB.getInstance(context);
                noteEntities = db.getNoteDao().getAllAsync();
                NotesRemoteViewsService.updateWidget(context, noteEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null)
            mBackgroundTask.cancel(true);
        return true;
    }
}
