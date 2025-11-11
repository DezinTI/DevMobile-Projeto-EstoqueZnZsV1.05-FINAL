package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.example.aula21.model.entity.Pedido
import com.example.aula21.model.entity.PedidoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido): Long

    @Update
    suspend fun update(pedido: Pedido)

    @Delete
    suspend fun delete(pedido: Pedido)

    @Query("SELECT * FROM pedidos_tb ORDER BY id DESC")
    fun getAll(): Flow<List<Pedido>>

    @Query("SELECT * FROM pedidos_tb ORDER BY id DESC")
    suspend fun getAllOnce(): List<Pedido>

    @Query("SELECT * FROM pedidos_tb WHERE id = :id")
    suspend fun getById(id: Int): Pedido?

    @Query("UPDATE pedidos_tb SET status = :status, total = :total WHERE id = :id")
    suspend fun setStatusAndTotal(id: Int, status: String, total: Double)

    @Query("UPDATE pedidos_tb SET status = :status WHERE id = :id")
    suspend fun setStatusOnly(id: Int, status: String)
}
