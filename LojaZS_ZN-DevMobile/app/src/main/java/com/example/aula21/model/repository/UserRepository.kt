package com.example.aula21.model.repository

import android.content.Context
import com.example.aula21.model.entity.User
import com.example.aula21.model.remote.RetrofitProvider

class UserRepository(private val context: Context) {

    private val db = AppDatabase.getDataBase(context)
    private val userDAO = db.getUserDAO()
    private val api = RetrofitProvider.api

    suspend fun repoHasUsers() {
        if (userDAO.countUsers() == 0) {
            val remote = api.getUsers()
            val local = remote.map { User(it.id, it.name, it.username, it.email) }
            userDAO.insertAllUsers(local)
        }
    }

    suspend fun userAuth(username: String, email: String) : User? {
        return userDAO.findUser(username, email)
    }

}