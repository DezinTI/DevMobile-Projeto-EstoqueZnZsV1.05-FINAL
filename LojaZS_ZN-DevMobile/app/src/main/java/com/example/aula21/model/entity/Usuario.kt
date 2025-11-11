package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios_tb")
data class Usuario(
    @PrimaryKey
    val id: String,
    val nome: String,
    val email: String,
    val cpf: String
)
