package com.example.aula21.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula21.model.entity.Todo
import com.example.aula21.model.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(context: Context) : ViewModel() {
    private val repoTodos = TodoRepository(context)

    var loading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    var listTodos = mutableStateOf<List<Todo>>(emptyList())
        private set

    fun loadTodos(userId: Int) {
        viewModelScope.launch {
            loading.value = true
            error.value = null
            try {
                listTodos.value = repoTodos.getTodosByUser(userId)
            } catch (e: Exception) {
                error.value = "Falha ao carregar tarefas do usuário..."
            } finally {
                loading.value = false
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repoTodos.delete(todo)
            // fallback: repoTodos.deleteById(todo.id)
            val listUpdated = repoTodos.getTodosByUser(todo.userId)
            // avisar a UIO que houve alteração
            withContext(Dispatchers.Main) { listTodos.value = listUpdated }
        }
    }

    fun changeStatus(todo: Todo) {
        viewModelScope.launch {
            try {
                repoTodos.uptadeCompletedStatus(todo)
                loadTodos(todo.userId)
            }catch (e: Exception) {
                error.value = "Erro aoa tualizar status"
            }
        }
    }

}