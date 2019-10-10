package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.stickynotes.DB.NoteRepository;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final NoteRepository mRepository;

    public AddNoteViewModelFactory(NoteRepository repository) {
        mRepository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNoteViewModel(mRepository);
    }
}
