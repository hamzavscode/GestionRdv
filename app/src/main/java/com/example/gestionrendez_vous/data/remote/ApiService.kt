package com.example.gestionrendez_vous.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/todos")
    suspend fun getTodos(): List<ApiTask>

    @GET("/todos/{id}")
    suspend fun getTodoById(@Path("id") id: Long): ApiTask
}
