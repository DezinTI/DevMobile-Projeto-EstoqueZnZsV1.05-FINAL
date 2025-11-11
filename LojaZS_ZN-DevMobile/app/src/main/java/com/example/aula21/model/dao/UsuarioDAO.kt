package com.example.aula21.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aula21.model.entity.Usuario

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(usuario: Usuario)

    @Query("SELECT * FROM usuarios_tb WHERE id = :id")
    suspend fun getById(id: String): Usuario?

    @Query("SELECT * FROM usuarios_tb WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getByEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios_tb WHERE cpf = :cpf LIMIT 1")
    suspend fun getByCpf(cpf: String): Usuario?

    @Query("SELECT * FROM usuarios_tb")
    suspend fun getAll(): List<Usuario>?
}
