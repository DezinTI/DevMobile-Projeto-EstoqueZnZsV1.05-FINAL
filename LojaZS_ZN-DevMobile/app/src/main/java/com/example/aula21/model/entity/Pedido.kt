package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos_tb")
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val criadoPor: String,
    val criadoEm: Long,
    val status: String, // rascunho, baixado
    val total: Double
)
