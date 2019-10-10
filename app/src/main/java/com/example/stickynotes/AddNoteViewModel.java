package com.example.stickynotes;

import androidx.lifecycle.ViewModel;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.DB.NoteRepository;

public class AddNoteViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public AddNoteViewModel(NoteRepository repository) {
        mNoteRepository = repository;
    }


    public void deleteNote(Note note) {
        mNoteRepository.deleteNote(note);
    }

    public void addNote(Note note) {
        mNoteRepository.addNote(note);
    }

    public void updateTask(Note note) {
        mNoteRepository.updateNote(note);
    }

}
