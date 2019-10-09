package com.example.stickynotes.DB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;

import java.util.Objects;

@Entity(tableName = "notes")
@Parcel
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long ID;
    @ColumnInfo(name = "note_content")
    private String content;
    private String title;

    public Note(String content, String title) {
        this.content = content;
        this.title = title;


    }

    @Ignore
    public Note() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note = (Note) o;

        if (getID() != note.getID()) return false;
        return Objects.equals(title, note.title);
    }


    @Override
    public int hashCode() {
        int result = (int) getID();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + getID() +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
