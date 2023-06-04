package com.smartdevapp.smartnote.TaskDatabase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.smartdevapp.smartnote.Models.Task;

import java.util.List;

@Dao
public interface TaskDataAccessObject {
    @Insert(onConflict = REPLACE)
    void insert(Task doTask);

    @Query("SELECT * FROM doTask ORDER BY TasklongClicked DESC")
    List<Task> getAll();

    @Query("UPDATE doTask SET title = :title,task = :task WHERE ID =:id")
    void  update(int id ,String title,String task);

    @Delete
    void delete(Task doTask);

    @Query("UPDATE doTask SET  Taskchecked = :TaskCheck WHERE ID = :id")
    void check(int id,boolean TaskCheck);

    @Query("UPDATE doTask SET  Taskboxed = :TaskBox WHERE ID = :id")
    void box(int id,boolean TaskBox);

    @Query("UPDATE doTask SET  TasklongClicked = :TaskLongClick WHERE ID = :id")
    void longClick(int id,boolean TaskLongClick);



}
