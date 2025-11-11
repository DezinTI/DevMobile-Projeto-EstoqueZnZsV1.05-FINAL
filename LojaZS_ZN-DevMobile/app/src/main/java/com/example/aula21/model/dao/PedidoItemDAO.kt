package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.example.aula21.model.entity.PedidoItem

@Dao
interface PedidoItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PedidoItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PedidoItem>)

    @Update
    suspend fun update(item: PedidoItem)

    @Delete
    suspend fun delete(item: PedidoItem)

    @Query("SELECT * FROM pedido_itens_tb WHERE pedidoId = :pedidoId")
    suspend fun getByPedido(pedidoId: Int): List<PedidoItem>

    @Query("DELETE FROM pedido_itens_tb WHERE pedidoId = :pedidoId")
    suspend fun deleteByPedido(pedidoId: Int)
}
