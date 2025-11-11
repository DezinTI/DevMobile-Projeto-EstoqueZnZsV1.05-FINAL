package com.example.aula21.utils

abstract class Helper {

    companion object {

        // Banco de dados
        const val DB_NAME = "todos_manager_db"

        // Tabela Users
        const val TABLE_USERS = "users_tb"
        const val USER_ID = "userId"
        const val USERNAME = "username"
        const val EMAIL = "email"

        // Tabela Todos
        const val TABLE_TODOS = "todos_tb"
        const val ID_TODO = "id"

        //  Retrofit
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}