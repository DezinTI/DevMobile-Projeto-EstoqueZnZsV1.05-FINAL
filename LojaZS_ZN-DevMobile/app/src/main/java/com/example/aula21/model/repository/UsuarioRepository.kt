package com.example.aula21.model.repository

import android.content.Context
import com.example.aula21.model.entity.Usuario

class UsuarioRepository(context: Context) {
    private val dao = AppDatabase.getDataBase(context).getUsuarioDAO()

    suspend fun upsert(usuario: Usuario) = dao.upsert(usuario)
    suspend fun obter(id: String): Usuario? = dao.getById(id)
    suspend fun buscarPorEmail(email: String): Usuario? = dao.getByEmail(email)
    suspend fun buscarPorCpf(cpf: String): Usuario? = dao.getByCpf(cpf)
    suspend fun listar(): List<Usuario> = dao.getAll() ?: emptyList()
}
