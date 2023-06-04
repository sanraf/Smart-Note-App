package com.smartdevapp.smartnote.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartdevapp.smartnote.Models.Notes;

import java.util.List;

@Dao
public interface MainDataAcessObject {
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :title,notes = :notes WHERE ID =:id")
    void  update(int id ,String title, String notes);

    @Query("UPDATE notes SET email = :email,password = :password WHERE ID =:id")
    void  update_security(int id ,String email, String password);


    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET  pinned = :pin WHERE ID = :id")
    void pin(int id,boolean pin);

    @Query("UPDATE notes SET  red = :redyed WHERE ID = :id")
    void red(int id,boolean redyed);

    @Query("UPDATE notes SET  pink = :pinked WHERE ID = :id")
    void pink(int id,boolean pinked);

    @Query("UPDATE notes SET  yellow = :yellowed WHERE ID = :id")
    void yellow(int id,boolean yellowed);

    @Query("UPDATE notes SET  creamy = :creamed WHERE ID = :id")
    void creamy(int id,boolean creamed);

    @Query("UPDATE notes SET  gray = :grayed WHERE ID = :id")
    void gray(int id,boolean grayed);

    @Query("UPDATE notes SET  green = :greened WHERE ID = :id")
    void green(int id,boolean greened);

    @Query("UPDATE notes SET  bold = :bold WHERE ID = :id")
    void bold(int id,boolean bold);

    @Query("UPDATE notes SET  italic = :italic WHERE ID = :id")
    void italic(int id,boolean italic);

    @Query("UPDATE notes SET  cursive = :cursive WHERE ID = :id")
    void cursive(int id,boolean cursive);

    @Query("UPDATE notes SET  casual = :casual WHERE ID = :id")
    void casual(int id,boolean casual);

    @Query("UPDATE notes SET  monospace = :monospace WHERE ID = :id")
    void monospace(int id,boolean monospace);

    @Query("UPDATE notes SET  locked = :locked WHERE ID = :id")
    void locked(int id,boolean locked);


    @Query("UPDATE notes SET  alarmed = :alarm WHERE ID = :id")
    void alarmed(int id,boolean alarm);

    @Query("UPDATE notes SET  longClicked = :longClick WHERE ID = :id")
    void longClick(int id,boolean longClick);






}
