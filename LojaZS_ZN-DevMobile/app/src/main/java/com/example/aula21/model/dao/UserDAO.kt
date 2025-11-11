package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aula21.model.entity.User
import com.example.aula21.utils.Helper


@Dao
interface UserDAO {

    @Query("SELECT COUNT(*) FROM ${Helper.TABLE_USERS}")
    suspend fun countUsers() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(listUser: List<User>)

    @Query("SELECT * FROM ${Helper.TABLE_USERS} " +
            "WHERE LOWER(${Helper.USERNAME}) = LOWER(:username) AND " +
            "LOWER(${Helper.EMAIL}) = LOWER(:email) LIMIT 1")
    suspend fun findUser(username: String, email: String): User?

}