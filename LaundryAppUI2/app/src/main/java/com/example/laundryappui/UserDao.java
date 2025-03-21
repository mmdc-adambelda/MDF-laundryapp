package com.example.laundryappui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void registerUser(UserEntity userEntity);

//    @Update
//    void update(UserEntity userEntity);

//    @Delete
//    void delete(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE username=(:username) and password=(:password)")
    UserEntity login(String username, String password);

    @Query("SELECT * FROM users WHERE username=(:username)")
    UserEntity getUserByUsername(String username);

//    @Query("SELECT * FROM users ORDER BY username ASC")
//    LiveData<List<UserEntity>> getAllUsers();
}
