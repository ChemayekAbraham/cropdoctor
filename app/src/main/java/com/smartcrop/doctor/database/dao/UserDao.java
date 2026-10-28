package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartcrop.doctor.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE LOWER(emailOrPhone) = LOWER(:emailOrPhone) LIMIT 1")
    User findByEmailOrPhone(String emailOrPhone);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User findById(long id);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<User> findByIdLiveData(long id);

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    LiveData<List<User>> getAllUsersLiveData();

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}
