package com.smartdevapp.smartnote.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "title")
    String title = "";

    @ColumnInfo(name = "notes")
    String notes = "";

    @ColumnInfo(name = "password")
    String password = "";

    @ColumnInfo(name = "email")
    String email = "";

    @ColumnInfo(name = "date")
    String date = "";

    @ColumnInfo(name = "pinned")
    boolean pinned = false;

    @ColumnInfo(name = "red")
    boolean red = false;

    @ColumnInfo(name = "pink")
    boolean pink = false;

    @ColumnInfo(name = "yellow")
    boolean yellow = false;

    @ColumnInfo(name = "creamy")
    boolean creamy = false;

    @ColumnInfo(name = "gray")
    boolean gray = false;

    @ColumnInfo(name = "green")
    boolean green = false;

    @ColumnInfo(name = "bold")
    boolean bold = false;

    @ColumnInfo(name = "italic")
    boolean italic = false;

    @ColumnInfo(name = "cursive")
    boolean cursive = false;

    @ColumnInfo(name = "casual")
    boolean casual = false;

    @ColumnInfo(name = "monospace")
    boolean monospace = false;

    @ColumnInfo(name = "locked")
    boolean locked = false;

    @ColumnInfo(name = "alarmed")
    boolean alarmed = false;

    @ColumnInfo(name = "longClicked")
    boolean longClicked = false;

    public Notes(int id, String note_title, String note_description, String note_time) {
        this.ID=id;
        this.title=note_title;
        this.notes=note_description;
        this.date=note_time;
    }
    public Notes(int id, String note_email, String note_password) {
        this.ID=id;
        this.email=note_email;
        this.password=note_password;

    }

    public Notes() {

    }

    public boolean isAlarmed() {
        return alarmed;
    }

    public void setAlarmed(boolean alarmed) {
        this.alarmed = alarmed;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public boolean isPink() {
        return pink;
    }

    public void setPink(boolean pink) {
        this.pink = pink;
    }

    public boolean isYellow() {
        return yellow;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isMonospace() {
        return monospace;
    }

    public void setMonospace(boolean monospace) {
        this.monospace = monospace;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isCursive() {
        return cursive;
    }

    public void setCursive(boolean cursive) {
        this.cursive = cursive;
    }

    public boolean isCasual() {
        return casual;
    }

    public void setCasual(boolean casual) {
        this.casual = casual;
    }

    public void setYellow(boolean yellow) {
        this.yellow = yellow;
    }

    public boolean isCreamy() {
        return creamy;
    }

    public void setCreamy(boolean creamy) {
        this.creamy = creamy;
    }

    public boolean isGray() {
        return gray;
    }

    public void setGray(boolean gray) {
        this.gray = gray;
    }

    public boolean isGreen() {
        return green;
    }

    public void setGreen(boolean green) {
        this.green = green;
    }

    public boolean isBlue() {
        return blue;
    }

    public void setBlue(boolean blue) {
        this.blue = blue;
    }

    public boolean isBrown() {
        return brown;
    }

    public void setBrown(boolean brown) {
        this.brown = brown;
    }

    @ColumnInfo(name = "blue")
    boolean blue = false;

    @ColumnInfo(name = "brown")
    boolean brown = false;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public boolean isLongClicked() {
        return longClicked;
    }

    public void setLongClicked(boolean longClicked) {
        this.longClicked = longClicked;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}