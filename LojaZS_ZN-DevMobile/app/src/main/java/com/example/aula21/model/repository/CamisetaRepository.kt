package com.example.aula21.model.repository

import android.content.Context
import com.example.aula21.model.entity.Camiseta
import kotlinx.coroutines.flow.Flow

class CamisetaRepository(context: Context) {
    private val dao = AppDatabase.getDataBase(context).getCamisetaDAO()

    fun listar(usuarioId: String): Flow<List<Camiseta>> = dao.getAll(usuarioId)
    fun buscar(usuarioId: String, q: String): Flow<List<Camiseta>> = dao.search(usuarioId, q)
    suspend fun obter(id: Int): Camiseta? = dao.getById(id)
    suspend fun getByExactNameOnce(nome: String): Camiseta? = dao.getByExactNameOnce(nome)
    suspend fun salvar(c: Camiseta): Long = dao.insert(c)
    suspend fun atualizar(c: Camiseta) = dao.update(c)
    suspend fun excluir(c: Camiseta) = dao.delete(c)
    suspend fun contar(usuarioId: String): Int = dao.countByUsuario(usuarioId)
    suspend fun salvarLista(list: List<Camiseta>) = dao.insertAll(list)
    suspend fun incrementarQuantidade(id: Int, delta: Int): Int = dao.incrementStock(id, delta)
    suspend fun decrementarQuantidade(id: Int, delta: Int): Int = dao.decrementStock(id, delta)
}
