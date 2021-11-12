package com.mustafa.mynotes.listeners;

import android.view.View;

import com.mustafa.mynotes.entities.Note;

public interface NoteLongClickListener {
    void onNoteLongClickListener(Note note, int position, View view);
}
