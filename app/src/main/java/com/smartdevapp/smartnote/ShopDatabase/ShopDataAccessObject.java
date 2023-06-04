package com.smartdevapp.smartnote.ShopDatabase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartdevapp.smartnote.Models.Notes;
import com.smartdevapp.smartnote.Models.Shopping;

import java.util.List;

@Dao
public interface ShopDataAccessObject {
    @Insert(onConflict = REPLACE)
    void insert(Shopping shopping);

    @Query("SELECT * FROM shopping ORDER BY longClicked DESC")
    List<Shopping> getAll();

    @Query("UPDATE shopping SET item = :item,quantity = :quantity,price = :price,TotalPrice = :total WHERE ID =:id")
    void  update(int id ,String item, int quantity,double price,double total);

    @Delete
    void delete(Shopping shopping);

    @Query("UPDATE shopping SET  checked = :check WHERE ID = :id")
    void check(int id,boolean check);

    @Query("UPDATE shopping SET  boxed = :box WHERE ID = :id")
    void box(int id,boolean box);

    @Query("UPDATE shopping SET  longClicked = :longClick WHERE ID = :id")
    void longClick(int id,boolean longClick);

    @Query("SELECT SUM("+"TotalPrice"+") FROM shopping")
    double getAllTotal();

    @Query("SELECT SUM("+"TotalItems"+") FROM shopping")
    int getAllTotalItems();


}
