package com.example.stickynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.stickynotes.DB.Note;

import org.parceler.Parcels;

public class AddNoteActivity extends AppCompatActivity {
    private EditText titleEditTxt, descrEditTxt;
    private Button addNoteBtn;
    private Note note;
    private boolean update;
    private Intent intent;
    private Toolbar toolbar;
    private AddNoteViewModel mAddNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleEditTxt = findViewById(R.id.titleEditTxt);
        descrEditTxt = findViewById(R.id.descrEditTxt);
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
        note = Parcels.unwrap(intent.getParcelableExtra(getString(R.string.note_parcel)));
        mAddNoteViewModel = obtainViewModel();
        if (note != null) {
            getSupportActionBar().setTitle(getString(R.string.update_note_title));
            update = true;
            addNoteBtn.setText(getString(R.string.update));
            titleEditTxt.setText(note.getTitle());
            descrEditTxt.setText(note.getContent());
        }
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    note.setContent(descrEditTxt.getText().toString());
                    note.setTitle(titleEditTxt.getText().toString());
                    mAddNoteViewModel.updateTask(note);
                    finish();
                } else {
                    note = new Note(descrEditTxt.getText().toString(), titleEditTxt.getText().toString());
                    mAddNoteViewModel.addNote(note);
                    finish();
                }
            }
        });
    }

    private AddNoteViewModel obtainViewModel() {
        AddNoteViewModelFactory viewModelFactory = Injector.provideDetailViewModelFactory(this);
        return ViewModelProviders.of(this, viewModelFactory).get(AddNoteViewModel.class);
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
            mAddNoteViewModel.deleteNote(note);
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
