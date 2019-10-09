package com.example.stickynotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotes.Adapter.NotesAdapter;
import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteDB;
import com.example.stickynotes.widget.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClick {
    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private NoteDB noteDatabase;
    private List<Note> notes;
    private NotesAdapter notesAdapter;
    private int pos;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initializeVies();
        displayList();
        Reminder.scheduleUpdateWidgetReminder(this);
    }

    private void displayList() {
        noteDatabase = NoteDB.getInstance(MainActivity.this);
        new RetrieveTask(this).execute();
    }

    @Override
    protected void onResume() {
        displayList();
        super.onResume();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().noteDatabase.getNoteDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                // hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private static class DeleteAllNotes extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        DeleteAllNotes(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityReference.get().noteDatabase.getNoteDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activityReference.get().notes.clear();
            activityReference.get().notesAdapter.notifyDataSetChanged();
            activityReference.get().textViewMsg.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }

    private void initializeVies() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewMsg = findViewById(R.id.tv__empty);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        notes = new ArrayList<>();
        notesAdapter = new NotesAdapter(notes, MainActivity.this);
        recyclerView.setAdapter(notesAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            new DeleteAllNotes(MainActivity.this).execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClick(final int pos) {
        MainActivity.this.pos = pos;
        startActivity(new Intent(MainActivity.this, AddNoteActivity.class).putExtra("note", Parcels.wrap(notes.get(pos))));
    }

    private void listVisibility() {
        int emptyMsgVisibility = View.GONE;
        if (notes.size() == 0) { // no item to display
            if (textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }
}

