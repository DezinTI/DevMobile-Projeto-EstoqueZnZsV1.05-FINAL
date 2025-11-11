package com.example.aula21.model.repository

import android.content.Context
import com.example.aula21.model.entity.Todo
import com.example.aula21.model.remote.RetrofitProvider

class TodoRepository(private val context: Context) {

    private val db = AppDatabase.getDataBase(context)
    private val todoDAO = db.getTodoDAO()
    private val api = RetrofitProvider.api

    suspend fun getTodosByUser(userdId: Int) : List<Todo> {
        val local = todoDAO.getTodoByUserId(userdId)
        if (local.isNotEmpty()) return local

        // se a lista estiver vazia, precisamos buscar do remoto
        val remote = api.getTodosByUser(userdId)
        val localMaped = remote.map { Todo(it.id, it.userId, it.title, it.completed) }
        todoDAO.insertAllTodos(localMaped)

        return todoDAO.getTodoByUserId(userdId)
    }

    suspend fun delete(todo: Todo){
        todoDAO.deleteTodo(todo)
    }

    suspend fun deleteById(id: Int) {
        todoDAO.deleteTodoById(id)
    }

    suspend fun uptadeCompletedStatus(todo: Todo) {
        todo.completed = !todo.completed
        todoDAO.updateCompletedStatus(todo)
    }


}