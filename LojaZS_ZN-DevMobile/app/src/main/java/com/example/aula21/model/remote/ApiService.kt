package com.example.aula21.model.remote

import com.example.aula21.model.dto.TodoDTO
import com.example.aula21.model.dto.UserDTO
import com.example.aula21.model.entity.Todo
import com.example.aula21.model.entity.User
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers() : List<UserDTO>

    @GET("todos")
    suspend fun getTodosByUser(@Query("userId") userId: Int): List<TodoDTO>

}