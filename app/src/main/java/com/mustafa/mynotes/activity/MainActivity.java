package com.mustafa.mynotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.RoomDatabase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mustafa.mynotes.activity.CreateNoteActivity;
import com.mustafa.mynotes.adapter.NoteAdapter;
import com.mustafa.mynotes.dao.NoteDao;
import com.mustafa.mynotes.database.NoteDatabase;
import com.mustafa.mynotes.databinding.ActivityMainBinding;
import com.mustafa.mynotes.entities.Note;
import com.mustafa.mynotes.listeners.NoteClickListener;
import com.mustafa.mynotes.listeners.NoteLongClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NoteClickListener, NoteLongClickListener {
    private ActivityMainBinding binding;
    List<Note> noteList;

    NoteDao noteDao;
    NoteDatabase database;
    private CompositeDisposable compositeDisposable;
    NoteAdapter adapter;
     Note receiverNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = (NoteDatabase) NoteDatabase.getInstance(this);
        noteList = new ArrayList<>();
        binding.noteCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });
        databaseGetALL();

    }

    public void databaseGetALL(){
        noteDao = database.noteDao();
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(noteDao.getAllNotes()
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlerNote)
        );
    }
    public void handlerNote(List<Note> note){
        if (note.size() > 0){
            noteList = note;
            adapter = new NoteAdapter(MainActivity.this,noteList,this::onNoteClickListener,this::onNoteLongClickListener);
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            binding.recyclerView.smoothScrollToPosition(0);
        }

    }

    // Tek tıklama metodu
    @Override
    public void onNoteClickListener(Note note, int position) {
       Intent intent = new Intent(MainActivity.this,CreateNoteActivity.class);
       intent.putExtra("info","old");
       intent.putExtra("notess",note);
       startActivity(intent);
    }

    // Çift tıklama metodu
    @Override
    public void onNoteLongClickListener(Note note, int position,View view) {
        receiverNote = note;
        openContextMenu(view);
        registerForContextMenu(view);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "Delete"){
            Note note = receiverNote;
            compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(noteDao.delete(note)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            );
            Toast.makeText(getApplicationContext(), "Delete basıldı", Toast.LENGTH_SHORT).show();
            Intent intent = MainActivity.this.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);


            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}