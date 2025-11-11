package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aula21.model.entity.Camiseta
import kotlinx.coroutines.flow.Flow

@Dao
interface CamisetaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(camiseta: Camiseta): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(camisetas: List<Camiseta>)

    @Update
    suspend fun update(camiseta: Camiseta)

    @Delete
    suspend fun delete(camiseta: Camiseta)

    @Query("SELECT * FROM camisetas_tb WHERE usuarioId = :usuarioId ORDER BY nome")
    fun getAll(usuarioId: String): Flow<List<Camiseta>>

    @Query("SELECT * FROM camisetas_tb WHERE usuarioId = :usuarioId ORDER BY nome")
    suspend fun getAllOnce(usuarioId: String): List<Camiseta>

    @Query("SELECT * FROM camisetas_tb WHERE id = :id")
    suspend fun getById(id: Int): Camiseta?

    @Query("SELECT * FROM camisetas_tb WHERE nome = :nome LIMIT 1")
    suspend fun getByExactNameOnce(nome: String): Camiseta?

    @Query("SELECT * FROM camisetas_tb WHERE usuarioId = :usuarioId AND (nome LIKE '%' || :q || '%' OR time LIKE '%' || :q || '%' OR tamanho LIKE '%' || :q || '%' OR CAST(id AS TEXT) LIKE '%' || :q || '%') ORDER BY nome")
    fun search(usuarioId: String, q: String): Flow<List<Camiseta>>

    @Query("DELETE FROM camisetas_tb WHERE usuarioId = :usuarioId")
    suspend fun deleteAllByUsuario(usuarioId: String)

    @Query("SELECT COUNT(*) FROM camisetas_tb WHERE usuarioId = :usuarioId")
    suspend fun countByUsuario(usuarioId: String): Int

    @Query("UPDATE camisetas_tb SET quantidade = quantidade - :delta WHERE id = :id AND quantidade >= :delta")
    suspend fun decrementStock(id: Int, delta: Int): Int

    @Query("UPDATE camisetas_tb SET quantidade = quantidade + :delta WHERE id = :id")
    suspend fun incrementStock(id: Int, delta: Int): Int
}
