package com.example.gestionrendez_vous.api

import retrofit2.Call
import retrofit2.http.GET

data class Rdv(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)

interface RdvService {
    @GET("/todos")
    fun getRendezVous(): Call<List<Rdv>>
}
