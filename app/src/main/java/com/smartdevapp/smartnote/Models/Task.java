package com.smartdevapp.smartnote.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "doTask")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "date")
    String date = "";

    @ColumnInfo(name = "title")
    String title = "";

    @ColumnInfo(name = "task")
    String task = "";

    @ColumnInfo(name = "Taskchecked")
    boolean Taskchecked = false;

    @ColumnInfo(name = "Taskboxed")
    boolean Taskboxed = false;

    @ColumnInfo(name = "TasklongClicked")
    boolean TasklongClicked = false;

    private boolean expandable = false;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isTaskchecked() {
        return Taskchecked;
    }

    public void setTaskchecked(boolean taskchecked) {
        Taskchecked = taskchecked;
    }

    public boolean isTaskboxed() {
        return Taskboxed;
    }

    public void setTaskboxed(boolean taskboxed) {
        Taskboxed = taskboxed;
    }

    public boolean isTasklongClicked() {
        return TasklongClicked;
    }

    public void setTasklongClicked(boolean tasklongClicked) {
        TasklongClicked = tasklongClicked;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
