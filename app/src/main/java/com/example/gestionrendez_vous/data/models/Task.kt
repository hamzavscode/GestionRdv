package com.example.gestionrendez_vous.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: Long, // ID coming from JSONPlaceholder, so not auto-generated
    val title: String,
    val priority: String = "Medium", // High, Medium, Low (Added locally)
    val dueDate: Long? = null, // Local addition
    val isCompleted: Boolean
)
