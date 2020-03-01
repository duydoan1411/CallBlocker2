package com.DD141.callblocker.ContactDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    void insertContact(Contact... contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAllContact();

    @Update
    void updateContact(Contact... contacts);

    @Query("Select * from  contacts")
    LiveData<List<Contact>> getAllContact();

    @Query("Select * from contacts where id = :id")
    Contact getContact(int id);

    @Query("Select * from contacts")
    List<Contact> getAllContactNonLiveData();
}
