package com.example.aula21.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula21.model.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SyncViewModel(context: Context) : ViewModel() {
    private val repo = SyncRepository(context)

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun pull(userId: String) {
        _loading.value = true
        _status.value = "Sincronizando (baixando)..."
        viewModelScope.launch {
            try {
                repo.pull(userId)
                _status.value = "Pull concluído"
            } catch (e: Exception) {
                _status.value = "Erro no pull: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun push(userId: String) {
        _loading.value = true
        _status.value = "Sincronizando (enviando)..."
        viewModelScope.launch {
            try {
                repo.push(userId)
                _status.value = "Push concluído"
            } catch (e: Exception) {
                _status.value = "Erro no push: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
