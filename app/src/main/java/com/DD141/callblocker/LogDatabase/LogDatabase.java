package com.DD141.callblocker.LogDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Log.class}, version = LogDatabase.DATABASE_VERSION, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LogDatabase extends RoomDatabase {
    private static LogDatabase sLogDatabase;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Room-database-log";

    public abstract LogDAO logDAO();

    public static LogDatabase getInstance(Context context){
        if (sLogDatabase == null){
            sLogDatabase = Room.databaseBuilder(context.getApplicationContext(), LogDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return sLogDatabase;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
