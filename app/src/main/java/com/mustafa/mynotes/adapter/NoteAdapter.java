package com.mustafa.mynotes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.mynotes.databinding.ItemRecyclerviewLayoutBinding;
import com.mustafa.mynotes.entities.Note;
import com.mustafa.mynotes.listeners.NoteClickListener;
import com.mustafa.mynotes.listeners.NoteLongClickListener;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    List<Note> noteList;
    Context context;
    NoteClickListener noteClickListener;
    NoteLongClickListener noteLongClickListener;
    public NoteAdapter(Context context, List<Note> noteList, NoteClickListener noteClickListener, NoteLongClickListener noteLongClickListener){
        this.noteList = noteList;
        this.context = context;
        this.noteClickListener = noteClickListener;
        this.noteLongClickListener = noteLongClickListener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerviewLayoutBinding binding =
                ItemRecyclerviewLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
      holder.setBinding(noteList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private ItemRecyclerviewLayoutBinding binding;
        public NoteHolder(ItemRecyclerviewLayoutBinding itemRecyclerviewLayoutBinding) {
            super(itemRecyclerviewLayoutBinding.getRoot());
            binding = itemRecyclerviewLayoutBinding;
        }
        public void setBinding(Note note,int position){
            binding.recyclerTitle.setText(note.getNote_title());
            binding.recyclerSubTitle.setText(note.getNote_subtitle());
            binding.recyclerDateTime.setText(note.getDate_time());
            GradientDrawable gradientDrawable = (GradientDrawable) binding.recyclerLayout.getBackground();
            gradientDrawable.setColor(Color.parseColor(note.getNote_color()));
            binding.recyclerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (noteClickListener != null){
                        noteClickListener.onNoteClickListener(noteList.get(position),position);
                    }
                }
            });

            binding.recyclerLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (noteLongClickListener != null){
                        noteLongClickListener.onNoteLongClickListener(noteList.get(position),position,v);
                    }
                    return true;
                }
            });

            if (note.getNote_imageurl() != null){
                Bitmap bitmap  = BitmapFactory.decodeFile(note.getNote_imageurl());
                binding.recyclerImageAdd.setImageBitmap(bitmap);
            }
            else{
                binding.recyclerImageAdd.setVisibility(View.GONE);
            }





        }
    }
}
