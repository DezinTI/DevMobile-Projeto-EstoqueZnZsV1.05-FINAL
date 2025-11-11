package com.example.aula21.model.repository

import android.content.Context
import androidx.room.withTransaction
import com.example.aula21.model.dao.CamisetaDAO
import com.example.aula21.model.dao.PedidoDAO
import com.example.aula21.model.dao.PedidoItemDAO
import com.example.aula21.model.entity.Pedido
import com.example.aula21.model.entity.PedidoItem
import kotlinx.coroutines.flow.Flow

class PedidoRepository(context: Context) {
    private val db = AppDatabase.getDataBase(context)
    private val pedidoDao: PedidoDAO = db.getPedidoDAO()
    private val itemDao: PedidoItemDAO = db.getPedidoItemDAO()
    private val camisetaDao: CamisetaDAO = db.getCamisetaDAO()

    fun listar(): Flow<List<Pedido>> = pedidoDao.getAll()
    suspend fun obter(id: Int): Pedido? = pedidoDao.getById(id)
    suspend fun itensDoPedido(pedidoId: Int): List<PedidoItem> = itemDao.getByPedido(pedidoId)

    suspend fun criarRascunho(criadoPor: String): Int {
        val id = pedidoDao.insert(
            Pedido(id = 0, criadoPor = criadoPor, criadoEm = System.currentTimeMillis(), status = "rascunho", total = 0.0)
        ).toInt()
        return id
    }

    suspend fun salvarItens(pedidoId: Int, itens: List<PedidoItem>) {
        // apaga antigos e insere novos
        itemDao.deleteByPedido(pedidoId)
        itemDao.insertAll(itens)
        val total = itens.sumOf { it.subtotal }
        pedidoDao.setStatusAndTotal(pedidoId, "rascunho", total)
    }

    suspend fun excluir(pedido: Pedido) {
        itemDao.deleteByPedido(pedido.id)
        pedidoDao.delete(pedido)
    }

    suspend fun baixarPedido(pedidoId: Int): Result<Unit> = runCatching {
        db.withTransaction {
            val pedido = pedidoDao.getById(pedidoId) ?: error("Pedido não encontrado")
            val itens = itemDao.getByPedido(pedidoId)
            require(itens.isNotEmpty()) { "Pedido sem itens. Adicione itens para baixar." }
            // validar saldos
            for (it in itens) {
                val ok = camisetaDao.decrementStock(it.camisetaId, it.quantidade)
                if (ok == 0) error("Saldo insuficiente para o item ${it.nomeCamiseta} (id=${it.camisetaId}).")
            }
            pedidoDao.setStatusAndTotal(pedidoId, "baixado", itens.sumOf { it.subtotal })
        }
    }

    suspend fun faturarPedido(pedidoId: Int): Result<Unit> = runCatching {
        val pedido = pedidoDao.getById(pedidoId) ?: error("Pedido não encontrado")
        require(pedido.status == "baixado") { "Só é possível faturar pedidos baixados." }
        pedidoDao.setStatusOnly(pedidoId, "faturado")
    }

    suspend fun fecharPedido(pedidoId: Int): Result<Unit> = runCatching {
        val pedido = pedidoDao.getById(pedidoId) ?: error("Pedido não encontrado")
        require(pedido.status == "faturado") { "Só é possível fechar pedidos faturados." }
        pedidoDao.setStatusOnly(pedidoId, "fechado")
    }
}
