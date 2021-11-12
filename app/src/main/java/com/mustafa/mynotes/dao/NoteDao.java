package com.mustafa.mynotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mustafa.mynotes.entities.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface NoteDao{

    @Query("SELECT * FROM notes ORDER BY id DESC")
    Flowable<List<Note>> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Note note);

    @Delete
    Completable delete(Note note);


}
