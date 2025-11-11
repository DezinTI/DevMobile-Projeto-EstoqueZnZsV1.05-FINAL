package com.example.aula21.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aula21.model.entity.User
import com.example.aula21.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(context: Context) : ViewModel() {

    private val userRepo = UserRepository(context)

    // variãveis que se comunicarão com a view (ui)
    var loading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    var loggedUser = mutableStateOf<User?>(null)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.repoHasUsers() // tentar alimentar o repo local com dados da api
            } catch (e: Exception) {
                Log.d("Erro BD/API", "Erro ao 'seedar' o repo local: ${e.message}")
            }
        }
    }

    fun login(usermame: String, email: String)  {
        viewModelScope.launch {
            loading.value = true
            error.value = null
            val user = userRepo.userAuth(usermame, email)

            if (user == null) {
                error.value = "Usuário não encontrado!"
            } else {
                loggedUser.value = user
            }
        }
    }

    fun clearAuth() {
        loggedUser.value = null
    }


}