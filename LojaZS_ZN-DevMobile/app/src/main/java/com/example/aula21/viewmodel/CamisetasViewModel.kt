package com.example.aula21.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula21.model.entity.Camiseta
import com.example.aula21.model.repository.CamisetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CamisetasViewModel(context: Context) : ViewModel() {
    private val repo = CamisetaRepository(context)

    // Enquanto Firebase não está integrado, usamos um usuarioId estático
    private val usuarioId = MutableStateFlow("local")
    private val query = MutableStateFlow("")
    private val _selecionada = MutableStateFlow<Camiseta?>(null)
    val selecionada: StateFlow<Camiseta?> = _selecionada

    val camisetas: StateFlow<List<Camiseta>> = query.flatMapLatest { q ->
        val trimmed = q.trim()
        val isPaddedId = trimmed.matches(Regex("^0*\\d+$")) && trimmed.isNotEmpty()
        if (trimmed.isBlank()) {
            repo.listar(usuarioId.value)
        } else if (isPaddedId) {
            val idInt = trimmed.toInt()
            flow {
                val item = repo.obter(idInt)
                emit(listOfNotNull(item))
            }
        } else {
            repo.buscar(usuarioId.value, trimmed)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val termoBusca: StateFlow<String> = query

    fun atualizarBusca(q: String) { query.value = q }

    fun setUsuario(id: String) {
        usuarioId.value = id
        viewModelScope.launch { seedIfEmpty() }
    }

    fun salvar(
        id: Int?, nome: String, time: String, tamanho: String,
        quantidade: Int, preco: Double, imagemUrl: String?
    ) {
        viewModelScope.launch {
            val c = Camiseta(
                id = id ?: 0,
                nome = nome,
                time = time,
                tamanho = tamanho,
                quantidade = quantidade,
                preco = preco,
                imagemUrl = imagemUrl,
                usuarioId = usuarioId.value
            )
            if (id == null || id <= 0) repo.salvar(c) else repo.atualizar(c)
        }
    }

    fun excluir(c: Camiseta) {
        viewModelScope.launch { repo.excluir(c) }
    }

    fun carregarPorId(id: Int) {
        viewModelScope.launch {
            _selecionada.value = repo.obter(id)
        }
    }

    fun entradaEstoque(id: Int, delta: Int) {
        if (delta <= 0) return
        viewModelScope.launch {
            repo.incrementarQuantidade(id, delta)
            _selecionada.value = repo.obter(id)
        }
    }

    init {
        viewModelScope.launch { seedIfEmpty() }
    }

    private suspend fun seedIfEmpty() {
        val uid = usuarioId.value
        val count = repo.contar(uid)
        if (count > 0) return
        val clubes = listOf(
            "Paraná Clube", "Athletico Paranaense", "Corinthians", "Barcelona",
            "Real Madrid", "Chelsea", "Santos", "Paysandu", "Vasco", "Inter Miami"
        )
        val tamanhos = listOf("PP", "M", "G", "GG", "XL", "XLL")
        val lista = mutableListOf<Camiseta>()
        var idAuto = 0
        clubes.forEach { clube ->
            tamanhos.forEach { tam ->
                idAuto += 1
                lista.add(
                    Camiseta(
                        id = 0,
                        nome = "$clube $tam",
                        time = clube,
                        tamanho = tam,
                        quantidade = (1..20).random(),
                        preco = listOf(149.9, 199.9, 249.9).random(),
                        imagemUrl = null,
                        usuarioId = uid
                    )
                )
            }
        }
        repo.salvarLista(lista)
    }
}
