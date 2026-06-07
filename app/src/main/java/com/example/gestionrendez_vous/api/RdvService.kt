package com.example.gestionrendez_vous.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Modèle pour l'API (différent de notre entité Room)
data class ApiRdv(
    val id: Int? = null,
    val userId: Int = 1,
    val title: String,
    val completed: Boolean = false
)

interface RdvService {
    @GET("/todos")
    suspend fun getRendezVous(): List<ApiRdv>

    @GET("/todos/{id}")
    suspend fun getRendezVousById(@Path("id") id: Int): ApiRdv

    @POST("/todos")
    suspend fun createRendezVous(@Body rdv: ApiRdv): ApiRdv

    @DELETE("/todos/{id}")
    suspend fun deleteRendezVous(@Path("id") id: Int)
}
