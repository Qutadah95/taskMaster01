package com.example.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {
    @Query("SELECT * FROM task")
    List<Task> getAll();


        @Query("Select * FROM task WHERE id = :id")
    Task getTaskById(long id);
    @Insert
    void insertAll(Task... tasks);


}