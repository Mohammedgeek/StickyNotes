package com.example.stickynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotes.Adapter.NotesAdapter;
import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteDB;
import com.example.stickynotes.widget.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClick {
    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private NoteDB noteDatabase;
    private NotesAdapter notesAdapter;
    private int pos;
    private MainViewModel mMainViewModel;
    private List<Note> mAllNotes = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        noteDatabase = NoteDB.getInstance(MainActivity.this);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getAllTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (notes.isEmpty()) {
                    mAllNotes = new ArrayList<>();
                    textViewMsg.setVisibility(View.VISIBLE);
                    notesAdapter.submitList(notes);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    mAllNotes = notes;
                    textViewMsg.setVisibility(View.GONE);
                    notesAdapter.submitList(notes);
                    notesAdapter.notifyDataSetChanged();
                }
            }
        });
        initializeVies();
        Reminder.scheduleUpdateWidgetReminder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        notesAdapter = new NotesAdapter(mAllNotes, MainActivity.this);
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
            mMainViewModel.deleteAllTasks();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClick(final int pos) {
        MainActivity.this.pos = pos;
        startActivity(new Intent(MainActivity.this, AddNoteActivity.class).putExtra(getString(R.string.note_parcel), Parcels.wrap(mAllNotes.get(pos))));
    }


}

