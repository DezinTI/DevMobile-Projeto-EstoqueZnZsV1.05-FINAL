package com.example.aula21.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula21.model.entity.Camiseta
import com.example.aula21.model.entity.Pedido
import com.example.aula21.model.entity.PedidoItem
import com.example.aula21.model.repository.CamisetaRepository
import com.example.aula21.model.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosViewModel(context: Context) : ViewModel() {
    private val pedidosRepo = PedidoRepository(context)
    private val camisRepo = CamisetaRepository(context)

    val pedidos: StateFlow<List<Pedido>> = pedidosRepo.listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selecionado = MutableStateFlow<Pedido?>(null)
    val selecionado: StateFlow<Pedido?> = _selecionado.asStateFlow()

    private val _itensTemp = MutableStateFlow<List<PedidoItem>>(emptyList())
    val itensTemp: StateFlow<List<PedidoItem>> = _itensTemp.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem.asStateFlow()

    fun carregarPedido(id: Int) {
        viewModelScope.launch {
            _selecionado.value = pedidosRepo.obter(id)
            if (_selecionado.value != null) {
                _itensTemp.value = pedidosRepo.itensDoPedido(id)
            }
        }
    }

    fun limparMensagens() { _erro.value = null; _mensagem.value = null }

    fun novoRascunho(criadoPor: String, onCreated: (Int) -> Unit) {
        viewModelScope.launch {
            val id = pedidosRepo.criarRascunho(criadoPor)
            _selecionado.value = pedidosRepo.obter(id)
            _itensTemp.value = emptyList()
            onCreated(id)
        }
    }

    fun adicionarItemPorIdOuNome(entrada: String, quantidade: Int) {
        viewModelScope.launch {
            limparMensagens()
            if (quantidade <= 0) { _erro.value = "Quantidade deve ser maior que zero"; return@launch }
            val c: Camiseta? = entrada.toIntOrNull()?.let { camisRepo.obter(it) }
                ?: camisRepo.getByExactNameOnce(entrada)
            if (c == null) { _erro.value = "Item não encontrado pelo ID ou nome"; return@launch }
            val item = PedidoItem(
                id = 0,
                pedidoId = _selecionado.value?.id ?: 0,
                camisetaId = c.id,
                nomeCamiseta = c.nome,
                precoUnit = c.preco,
                quantidade = quantidade,
                subtotal = c.preco * quantidade
            )
            _itensTemp.value = _itensTemp.value + item
        }
    }

    fun removerItemTemp(index: Int) {
        if (index < 0 || index >= _itensTemp.value.size) return
        _itensTemp.value = _itensTemp.value.toMutableList().also { it.removeAt(index) }
    }

    fun atualizarItemTemp(index: Int, novaQuantidade: Int) {
        if (index < 0 || index >= _itensTemp.value.size) return
        if (novaQuantidade <= 0) { _erro.value = "Quantidade deve ser maior que zero"; return }
        val atual = _itensTemp.value[index]
        val atualizado = atual.copy(quantidade = novaQuantidade, subtotal = atual.precoUnit * novaQuantidade)
        val lista = _itensTemp.value.toMutableList()
        lista[index] = atualizado
        _itensTemp.value = lista
    }

    fun salvarItens() {
        val ped = _selecionado.value ?: run { _erro.value = "Crie o pedido antes de salvar itens"; return }
        val itens = _itensTemp.value
        if (itens.isEmpty()) { _erro.value = "Pedido sem itens. Adicione itens"; return }
        viewModelScope.launch {
            // Persistimos os itens do pedido. O repositório recalcula e grava o total no pedido.
            pedidosRepo.salvarItens(ped.id, itens.map { it.copy(pedidoId = ped.id) })
            // Após salvar, recarregamos o pedido e seus itens do banco para refletir o total atualizado.
            _selecionado.value = pedidosRepo.obter(ped.id)
            _itensTemp.value = pedidosRepo.itensDoPedido(ped.id)
            _mensagem.value = "Itens salvos"
        }
    }

    fun baixarPedido() {
        val ped = _selecionado.value ?: run { _erro.value = "Crie o pedido antes de baixar"; return }
        viewModelScope.launch {
            val r = pedidosRepo.baixarPedido(ped.id)
            if (r.isSuccess) {
                _mensagem.value = "PEDIDO BAIXADO! OBRIGADO"
                _selecionado.value = pedidosRepo.obter(ped.id)
                _itensTemp.value = pedidosRepo.itensDoPedido(ped.id)
            } else {
                _erro.value = r.exceptionOrNull()?.message ?: "Erro ao baixar pedido"
            }
        }
    }

    fun faturarPedido() {
        val ped = _selecionado.value ?: run { _erro.value = "Selecione um pedido"; return }
        viewModelScope.launch {
            val r = pedidosRepo.faturarPedido(ped.id)
            if (r.isSuccess) {
                _mensagem.value = "Pedido faturado"
                _selecionado.value = pedidosRepo.obter(ped.id)
                _itensTemp.value = pedidosRepo.itensDoPedido(ped.id)
            } else {
                _erro.value = r.exceptionOrNull()?.message ?: "Erro ao faturar pedido"
            }
        }
    }

    fun fecharPedido() {
        val ped = _selecionado.value ?: run { _erro.value = "Selecione um pedido"; return }
        viewModelScope.launch {
            val r = pedidosRepo.fecharPedido(ped.id)
            if (r.isSuccess) {
                _mensagem.value = "Pedido fechado"
                _selecionado.value = pedidosRepo.obter(ped.id)
                _itensTemp.value = pedidosRepo.itensDoPedido(ped.id)
            } else {
                _erro.value = r.exceptionOrNull()?.message ?: "Erro ao fechar pedido"
            }
        }
    }

    fun excluirSelecionado() {
        val ped = _selecionado.value ?: return
        viewModelScope.launch {
            pedidosRepo.excluir(ped)
            _selecionado.value = null
            _itensTemp.value = emptyList()
            _mensagem.value = "Pedido excluído"
        }
    }
}
