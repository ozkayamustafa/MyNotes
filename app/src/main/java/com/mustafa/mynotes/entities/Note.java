package com.mustafa.mynotes.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "note_title")
    public String note_title;

    @ColumnInfo(name = "note_subtitle")
    public String note_subtitle;

    @ColumnInfo(name = "note_color")
   public String note_color;

    @ColumnInfo(name = "date_time")
   public String date_time;

    @ColumnInfo(name = "note_text")
    public String note_text;

    @ColumnInfo(name = "note_imageurl")
    public String note_imageurl;


    public String getNote_imageurl() {
        return note_imageurl;
    }

    public void setNote_imageurl(String note_imageurl) {
        this.note_imageurl = note_imageurl;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_subtitle() {
        return note_subtitle;
    }

    public void setNote_subtitle(String note_subtitle) {
        this.note_subtitle = note_subtitle;
    }

    public String getNote_color() {
        return note_color;
    }

    public void setNote_color(String note_color) {
        this.note_color = note_color;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
