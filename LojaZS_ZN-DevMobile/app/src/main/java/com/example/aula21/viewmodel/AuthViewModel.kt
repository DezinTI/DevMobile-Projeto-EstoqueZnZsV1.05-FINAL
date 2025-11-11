package com.example.aula21.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    init {
        _userId.value = null
    }

    fun login(email: String, password: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val username = email.trim()
            val pass = password.trim()
            val admin = (username == "dezin" && pass == "dezin123") || (username == "rick" && pass == "rick123")
            val userOk = admin || (username == "teste" && pass == "teste123")
            if (userOk) {
                _userId.value = username
                _isAdmin.value = admin
                _error.value = null
            } else {
                _userId.value = null
                _isAdmin.value = false
                _error.value = "Credenciais incorretas. Verifique usuário e senha."
            }
            _loading.value = false
        }
    }

    fun signUp(email: String, password: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            // cadastro desabilitado neste escopo; apenas login com contas fixas
                _userId.value = null
                _isAdmin.value = false
                _error.value = "Cadastro desabilitado. Use um dos usuários pré-definidos."
            _loading.value = false
        }
    }

    fun logout() {
        _userId.value = null
        _isAdmin.value = false
        _error.value = null
    }
}

