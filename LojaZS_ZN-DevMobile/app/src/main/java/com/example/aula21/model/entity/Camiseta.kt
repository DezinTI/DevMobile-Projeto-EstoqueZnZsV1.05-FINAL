package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camisetas_tb")
data class Camiseta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val time: String,
    val tamanho: String,
    val quantidade: Int,
    val preco: Double,
    val imagemUrl: String?,
    val usuarioId: String
)
