package com.smartdevapp.smartnote.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.smartdevapp.smartnote.Models.Notes;

@Database(entities  = Notes.class,version = 3,exportSchema = false)
public abstract class RoomDB  extends RoomDatabase {

    private static  RoomDB database;
    private  static final String DATABASE_NAME = "NoteApp";

    public  synchronized static  RoomDB getInstance(Context context){
        if(database==null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDataAcessObject mainDataAcessObject();

}
