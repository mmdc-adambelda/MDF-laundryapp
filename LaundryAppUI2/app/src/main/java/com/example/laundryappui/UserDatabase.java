package com.example.laundryappui;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String dbName = "user";
    private static volatile UserDatabase instance;
    public abstract UserDao userDao();

    public static UserDatabase getInstance(final Context context) {

        if (instance == null) {
            synchronized (UserDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, dbName).build();
                }
            }
        }

        return instance;
    }
}