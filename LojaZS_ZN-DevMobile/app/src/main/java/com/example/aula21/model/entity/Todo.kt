package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aula21.utils.Helper

@Entity (tableName = Helper.TABLE_TODOS)
data class Todo(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val userId: Int,
    val title: String,
    var completed: Boolean
)
