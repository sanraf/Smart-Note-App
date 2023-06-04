package com.smartdevapp.smartnote.ShopDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.smartdevapp.smartnote.Models.Shopping;

@Database(entities  = Shopping.class,version = 5,exportSchema = false)
public abstract class ShopRoomDB extends RoomDatabase {

    private static ShopRoomDB shopdatabase;
    private  static  String DATABASE_NAME = "Shopping_Database";

    public  synchronized static  ShopRoomDB getInstance(Context context){
        if(shopdatabase==null){
            shopdatabase = Room.databaseBuilder(context.getApplicationContext(),
                    ShopRoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return shopdatabase;
    }

    public abstract ShopDataAccessObject shopDataAccessObject();
}
