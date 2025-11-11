package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aula21.model.entity.Todo
import com.example.aula21.utils.Helper

@Dao
interface TodoDAO {

    @Query("SELECT COUNT(*) FROM ${Helper.TABLE_TODOS}")
    suspend fun countTodos() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTodos(listTodos: List<Todo>)

    @Query("SELECT * FROM ${Helper.TABLE_TODOS} " +
            "WHERE ${Helper.USER_ID} = :userId " +
            "ORDER BY ${Helper.ID_TODO} ASC")
    suspend fun getTodoByUserId(userId: Int) : List<Todo>

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("DELETE FROM ${Helper.TABLE_TODOS} " +
            "WHERE ${Helper.ID_TODO} = :id")
    suspend fun deleteTodoById(id: Int)

    @Update
    suspend fun updateCompletedStatus(todo: Todo)
}