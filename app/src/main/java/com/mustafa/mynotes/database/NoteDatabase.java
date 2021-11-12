package com.mustafa.mynotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mustafa.mynotes.dao.NoteDao;
import com.mustafa.mynotes.entities.Note;

@Database(entities = {Note.class}, version = 3)
public abstract class NoteDatabase extends RoomDatabase {
    private  static  RoomDatabase database;

    public static RoomDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context,NoteDatabase.class,"Notes").build();
        }
        return database;
    }
    public abstract NoteDao noteDao();
}
