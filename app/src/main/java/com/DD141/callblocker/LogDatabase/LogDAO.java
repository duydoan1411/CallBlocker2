package com.DD141.callblocker.LogDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDAO {

    @Insert
    void insertLog(Log... logs);

    @Delete
    void deleteLog(Log log);

    @Query("DELETE from logs")
    void deleteAllLog();

    @Update
    void updateLog(Log log);

    @Query("Select * from logs where id = :id")
    Log getLog(int id);

    @Query("Select * from logs order by date ASC")
    LiveData<List<Log>> getAllLog();

    @Query("Select * from logs order by date ASC")
    List<Log> getAllLogNonLiveData();

}
