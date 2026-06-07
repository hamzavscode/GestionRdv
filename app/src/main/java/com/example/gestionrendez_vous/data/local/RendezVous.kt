package com.example.gestionrendez_vous.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rendez_vous")
data class RendezVous(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titre: String,
    val description: String,
    val dateHeure: Long,
    val lieu: String,
    val imagePath: String? = null,
    val statut: String = "en_attente", // en_attente, confirme, annule
    val createdAt: Long = System.currentTimeMillis()
)
