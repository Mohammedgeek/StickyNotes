package com.example.stickynotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteDB;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;

public class AddNoteActivity extends AppCompatActivity {
    private EditText titleEditTxt, descrEditTxt;
    private Button addNoteBtn;
    private NoteDB noteDatabase;
    private Note note;
    private boolean update;
    private Intent intent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleEditTxt = findViewById(R.id.titleEditTxt);
        descrEditTxt = findViewById(R.id.descrEditTxt);
        noteDatabase = NoteDB.getInstance(AddNoteActivity.this);
        addNoteBtn = findViewById(R.id.saveBtn);
        toolbar = findViewById(R.id.secToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        intent = getIntent();
        note = Parcels.unwrap(intent.getParcelableExtra("note"));
        if (note != null) {
            getSupportActionBar().setTitle("Update Note");
            update = true;
            addNoteBtn.setText("Update");
            titleEditTxt.setText(note.getTitle());
            descrEditTxt.setText(note.getContent());
        }
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    note.setContent(descrEditTxt.getText().toString());
                    note.setTitle(titleEditTxt.getText().toString());
                    // noteDatabase.getNoteDao().update(note);
                    new UpdateNote(AddNoteActivity.this, note).execute();
                } else {
                    note = new Note(descrEditTxt.getText().toString(), titleEditTxt.getText().toString());
                    new InsertNote(AddNoteActivity.this, note).execute();
                }
            }
        });
    }

    private void setResult(Note note, int flag) {
        setResult(flag, new Intent().putExtra("note", (Parcelable) note));
        finish();
    }

    private static class InsertNote extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        InsertNote(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            activityReference.get().noteDatabase.getNoteDao().insert(note);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                // activityReference.get().setResult(note, 1);
                activityReference.get().finish();
            }
        }
    }

    private static class UpdateNote extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        UpdateNote(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            activityReference.get().noteDatabase.getNoteDao().update(note);
            Log.d("ID ", "Updated " + note.getID());
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().finish();
            }
        }
    }

    private static class DeleteNote extends AsyncTask<Void, Void, Void> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        DeleteNote(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityReference.get().noteDatabase.getNoteDao().delete(note);
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (note != null) {
            getMenuInflater().inflate(R.menu.note_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_note) {
            new DeleteNote(AddNoteActivity.this, note).execute();
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
