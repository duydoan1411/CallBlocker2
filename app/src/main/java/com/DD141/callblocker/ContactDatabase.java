package com.DD141.callblocker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@androidx.room.Database(entities = {Contact.class}, version = ContactDatabase.DATABASE_VERSION, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    private static ContactDatabase sContactDatabase;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Room-database";

    public abstract ContactDAO contactDAO();

    public static ContactDatabase getInstance(Context context){
        if (sContactDatabase == null){
            sContactDatabase = Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }

        return sContactDatabase;
    }
    private  static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
