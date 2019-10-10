package com.example.stickynotes.widget;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Prefs {

    public static void saveNotes(Context context, List<Note> tasks) {
        Gson gson = new Gson();
        String noteJsonString = gson.toJson(tasks);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferance), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.note_key), noteJsonString);
        editor.apply();
    }

    public static List<Note> loadNotes(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Note>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferance), Context.MODE_PRIVATE);
        String noteJsonString = sharedPreferences.getString(context.getString(R.string.note_key), "");
        return gson.fromJson(noteJsonString, type);
    }
}
