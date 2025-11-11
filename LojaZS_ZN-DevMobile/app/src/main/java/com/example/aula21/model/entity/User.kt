package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aula21.utils.Helper

@Entity (tableName = Helper.TABLE_USERS)
data class User(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)
