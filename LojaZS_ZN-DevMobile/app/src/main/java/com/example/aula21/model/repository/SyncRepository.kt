package com.example.aula21.model.repository

import android.content.Context
import com.example.aula21.model.entity.Camiseta

class SyncRepository(context: Context) {
    private val dao = AppDatabase.getDataBase(context).getCamisetaDAO()
    private val userRepo = UserRepository(context)
    private val todoRepo = TodoRepository(context)

    suspend fun pull(userId: String) {
        userRepo.repoHasUsers()
        val uid = userId.toIntOrNull() ?: 1
        todoRepo.getTodosByUser(uid)
    }

    suspend fun push(userId: String) {
    }
}
