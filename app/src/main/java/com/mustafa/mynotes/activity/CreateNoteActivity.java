package com.mustafa.mynotes.activity;

import static android.graphics.ImageDecoder.*;
import static android.graphics.ImageDecoder.decodeBitmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.mustafa.mynotes.R;
import com.mustafa.mynotes.dao.NoteDao;
import com.mustafa.mynotes.database.NoteDatabase;
import com.mustafa.mynotes.databinding.ActivityCreateNoteBinding;
import com.mustafa.mynotes.entities.Note;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateNoteActivity extends AppCompatActivity {
    private ActivityCreateNoteBinding binding;

    String selectedColor;

    private NoteDao noteDao;
    private NoteDatabase database;

    private CompositeDisposable compositeDisposable;
    Note alreadyAvailable;

    private String imagePath;

    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        database = (NoteDatabase) NoteDatabase.getInstance(this);
        getBackActivity();
        selectedColor = "#333333";
        imagePath ="";

        try {
            if (getIntent().getStringExtra("info").equals("old")){
                alreadyAvailable = (Note) getIntent().getSerializableExtra("notess");

                binding.inputTitle.setText(alreadyAvailable.getNote_title());
                selectedColor = alreadyAvailable.getNote_color();
                imagePath = alreadyAvailable.getNote_imageurl();
                alreadyAvailableMisscelenouıs(selectedColor); // BottomBehavior  renk kodarı hangisi şecili ise onu getiriyor
                binding.inputSubtitle.setText(alreadyAvailable.getNote_subtitle());
                binding.inputText.setText(alreadyAvailable.getNote_text());
                binding.noteDateTime.setText(alreadyAvailable.getDate_time());
                Log.d("Image", alreadyAvailable.getNote_imageurl());
                if (alreadyAvailable.getNote_imageurl() != null){
                    Uri getDataPath = Uri.parse(alreadyAvailable.getNote_imageurl());

                       /* Bitmap dataImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),getDataPath);
                        binding.inputImage.setImageBitmap(dataImage);
                        binding.inputImage.setVisibility(View.VISIBLE);
                       */
                    Bitmap bitmap = BitmapFactory.decodeFile(alreadyAvailable.getNote_imageurl());
                    binding.inputImage.setImageBitmap(bitmap);
                    binding.inputImage.setVisibility(View.VISIBLE);
                    binding.imageDeleteIcon.setVisibility(View.VISIBLE);

                }


                alreadyAvailableMisscelenouıs(selectedColor); // BottomBehavior  renk kodarı hangisi şecili ise onu getiriyor
                GradientDrawable gradientDrawable = (GradientDrawable) binding.subtitleColor.getBackground();
                gradientDrawable.setColor(Color.parseColor(alreadyAvailable.getNote_color()));
                binding.imageDeleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = null;
                        alreadyAvailable.setNote_imageurl(null);
                        binding.imageDeleteIcon.setVisibility(View.GONE);
                        binding.inputImage.setVisibility(View.GONE);
                    }
                });
                getBackActivity();

            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        bottomBehaviorShow();
        viewIndicatorColor();
        binding.imageDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = null;
                binding.imageDeleteIcon.setVisibility(View.GONE);
                binding.inputImage.setVisibility(View.GONE);
            }
        });
        clickCreateNote();
        initImageLauncher();


    }

    public void getBackActivity(){
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    public void alreadyAvailableMisscelenouıs(String colorCode){
        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ImageView colorDefault = linearLayout.findViewById(R.id.colorDefault);
        ImageView colorNote2 = linearLayout.findViewById(R.id.colorNote2);
        ImageView colorNote3 = linearLayout.findViewById(R.id.colorNote3);
        ImageView colorNote4 = linearLayout.findViewById(R.id.colorNote4);
        ImageView colorNote5 = linearLayout.findViewById(R.id.colorNote5);
        switch (colorCode){
            case "#FDBE3B":
                colorNote2.setImageResource(R.drawable.ic_create_note);
                colorNote3.setImageResource(0);
                colorNote4.setImageResource(0);
                colorNote5.setImageResource(0);
                colorDefault.setImageResource(0);
                break;
            case "#FF4842":
                colorNote3.setImageResource(R.drawable.ic_create_note);
                colorNote2.setImageResource(0);
                colorNote5.setImageResource(0);
                colorNote4.setImageResource(0);
                colorDefault.setImageResource(0);
                break;
            case "#333333":
                colorNote3.setImageResource(0);
                colorNote4.setImageResource(0);
                colorNote5.setImageResource(0);
                colorNote2.setImageResource(0);
                colorDefault.setImageResource(R.drawable.ic_create_note);
                break;
            case "#3A52Fc":
                colorNote4.setImageResource(R.drawable.ic_create_note);
                colorNote2.setImageResource(0);
                colorNote3.setImageResource(0);
                colorNote5.setImageResource(0);
                colorDefault.setImageResource(0);
                break;
            case "#000000":
                colorNote3.setImageResource(0);
                colorNote4.setImageResource(0);
                colorNote5.setImageResource(R.drawable.ic_create_note);
                colorNote2.setImageResource(0);
                colorDefault.setImageResource(0);
                break;
        }

    }

    public void bottomBehaviorShow(){
        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        linearLayout.findViewById(R.id.colorMissle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

           ImageView colorDefault = linearLayout.findViewById(R.id.colorDefault);
           ImageView colorNote2 = linearLayout.findViewById(R.id.colorNote2);
           ImageView colorNote3 = linearLayout.findViewById(R.id.colorNote3);
           ImageView colorNote4 = linearLayout.findViewById(R.id.colorNote4);
           ImageView colorNote5 = linearLayout.findViewById(R.id.colorNote5);

           colorNote2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedColor = "#FDBE3B";
                   colorNote2.setImageResource(R.drawable.ic_create_note);
                   colorNote3.setImageResource(0);
                   colorNote4.setImageResource(0);
                   colorNote5.setImageResource(0);
                   colorDefault.setImageResource(0);
                   viewIndicatorColor();
               }
           });
           colorNote3.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedColor = "#FF4842";
                   colorNote3.setImageResource(R.drawable.ic_create_note);
                   colorNote2.setImageResource(0);
                   colorNote5.setImageResource(0);
                   colorNote4.setImageResource(0);
                   colorDefault.setImageResource(0);
                   viewIndicatorColor();
               }
           });
           colorDefault.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedColor = "#333333";
                   colorNote3.setImageResource(0);
                   colorNote4.setImageResource(0);
                   colorNote5.setImageResource(0);
                   colorNote2.setImageResource(0);
                   colorDefault.setImageResource(R.drawable.ic_create_note);
                   viewIndicatorColor();
               }
           });
           colorNote4.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedColor = "#3A52Fc";
                   colorNote4.setImageResource(R.drawable.ic_create_note);
                   colorNote2.setImageResource(0);
                   colorNote3.setImageResource(0);
                   colorNote5.setImageResource(0);
                   colorDefault.setImageResource(0);
                   viewIndicatorColor();
               }
           });
           colorNote5.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedColor = "#000000";
                   colorNote3.setImageResource(0);
                   colorNote4.setImageResource(0);
                   colorNote5.setImageResource(R.drawable.ic_create_note);
                   colorNote2.setImageResource(0);
                   colorDefault.setImageResource(0);
                   viewIndicatorColor();
               }
           });

        linearLayout.findViewById(R.id.addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                if (ContextCompat.checkSelfPermission(CreateNoteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreateNoteActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Snackbar.make(v,"Permission Give Me",Snackbar.LENGTH_INDEFINITE)
                                .setAction("Give Me", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                                    }
                                });
                    }
                    else{
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultLauncher.launch(intent);
                }
            }
        });

    }
    public void viewIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) binding.subtitleColor.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));
    }


    public void clickCreateNote(){
        binding.imageNoteCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteInsert();
            }
        });
    }



    public void noteInsert(){
        if (isNoteEmpty()){
            Note note = new Note();
            note.setDate_time(dateTime(new Date()));
            note.setNote_title(binding.inputTitle.getText().toString());
            note.setNote_subtitle(binding.inputSubtitle.getText().toString());
            note.setNote_text(binding.inputText.getText().toString());
            if (selectedColor != null){
                note.setNote_color(selectedColor);
            }
            if (imagePath != null){
                note.setNote_imageurl(imagePath);
            }
            else{
                binding.inputImage.setVisibility(View.GONE);
                binding.imageDeleteIcon.setVisibility(View.GONE);
            }
            if (alreadyAvailable != null){
                note.setId(alreadyAvailable.getId());
            }
            noteDao = database.noteDao();
            compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(noteDao.insert(note)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            );
            Intent intent = new Intent(CreateNoteActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Note saved succesfully", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNoteEmpty(){
        if (binding.inputTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Title cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(binding.inputSubtitle.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Subtitle cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public String dateTime(Date date){
        return new SimpleDateFormat("EEEE,dd MMMM HH:MM a", Locale.getDefault()).format(date);
    }

    public void initImageLauncher(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intent = result.getData();
                    if (intent != null){
                        Uri uri = intent.getData();
                        imagePath = getSelectedPathUrl(uri);
                        if (Build.VERSION.SDK_INT>=26){
                            Source source = createSource(CreateNoteActivity.this.getContentResolver(),uri);
                            try {
                               Bitmap imageSelected = decodeBitmap(source);
                               binding.inputImage.setImageBitmap(imageSelected);
                               binding.inputImage.setVisibility(View.VISIBLE);
                               if (imagePath != null){
                                   binding.imageDeleteIcon.setVisibility(View.VISIBLE);
                               }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Bitmap selectedImage = null;
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.inputImage.setImageBitmap(selectedImage);
                            binding.inputImage.setVisibility(View.VISIBLE);
                            if (imagePath != null){
                                binding.imageDeleteIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                 if (result){
                     Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     resultLauncher.launch(intent);
                 }
            }
        });
    }

    private String getSelectedPathUrl(Uri uri){
        String filePaht = "";
        Cursor cursor = getContentResolver().query(uri,null,null,null
        );

        if (cursor == null){
            filePaht = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePaht = cursor.getString(index);
            cursor.close();
        }
        return filePaht;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null){
            compositeDisposable.clear();
        }
    }
}