package com.example.gestionrendez_vous.data.local

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long
}
