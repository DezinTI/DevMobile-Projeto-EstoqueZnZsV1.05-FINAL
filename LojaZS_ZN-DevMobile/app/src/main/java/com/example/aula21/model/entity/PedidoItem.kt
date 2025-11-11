package com.example.aula21.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido_itens_tb")
data class PedidoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pedidoId: Int,
    val camisetaId: Int,
    val nomeCamiseta: String,
    val precoUnit: Double,
    val quantidade: Int,
    val subtotal: Double
)
