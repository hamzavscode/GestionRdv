package com.example.gestionrendez_vous.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val email: String,
    val passwordHash: String
)
