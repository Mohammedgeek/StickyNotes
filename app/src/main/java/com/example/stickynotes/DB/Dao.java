package com.example.stickynotes.DB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM notes")
    List<Note> getAll();

    @Insert
    void insert(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Note repos);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM " + "notes")
    void deleteAll();
}
