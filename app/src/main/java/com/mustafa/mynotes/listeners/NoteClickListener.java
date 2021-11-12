package com.mustafa.mynotes.listeners;

import android.view.View;

import com.mustafa.mynotes.entities.Note;

public interface NoteClickListener {
    void onNoteClickListener(Note note, int position);
}
