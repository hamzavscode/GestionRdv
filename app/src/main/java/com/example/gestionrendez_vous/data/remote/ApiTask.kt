package com.example.gestionrendez_vous.data.remote

// Data Transfer Object for JSONPlaceholder /todos
data class ApiTask(
    val userId: Int,
    val id: Long,
    val title: String,
    val completed: Boolean
)
