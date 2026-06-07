package com.example.gestionrendez_vous.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientName: String,
    val clientId: Long? = null, // Optional reference to Client
    val date: String, // e.g., "YYYY-MM-DD"
    val time: String, // e.g., "14:30"
    val serviceType: String,
    val status: String // e.g., "Confirmed", "Pending", "Cancelled"
)
