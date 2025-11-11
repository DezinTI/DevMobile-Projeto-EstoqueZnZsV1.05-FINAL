package com.example.aula21.model.dto

data class TodoDTO(
    val id: Int,
    val userId: Int,
    val title: String,
    var completed: Boolean
)
