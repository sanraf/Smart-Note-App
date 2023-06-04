package com.smartdevapp.smartnote.TaskDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.smartdevapp.smartnote.Models.Task;

@Database(entities  = Task.class,version = 1,exportSchema = false)
public abstract class TaskRoomDB extends RoomDatabase {

    private static TaskRoomDB taskdatabase;
    private  static  String DATABASE_NAME = "task_Database";

    public  synchronized static TaskRoomDB getInstance(Context context){
        if(taskdatabase==null){
            taskdatabase = Room.databaseBuilder(context.getApplicationContext(),
                    TaskRoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return taskdatabase;
    }

    public abstract TaskDataAccessObject taskDataAccessObject();
}
