package com.example.gestionrendez_vous.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RendezVousDao {

    @Query("SELECT * FROM rendez_vous ORDER BY dateHeure DESC")
    fun getAll(): LiveData<List<RendezVous>>

    @Query("SELECT * FROM rendez_vous WHERE id = :id")
    suspend fun getById(id: Long): RendezVous?

    @Query("SELECT * FROM rendez_vous WHERE titre LIKE '%' || :query || '%' ORDER BY dateHeure DESC")
    fun searchByTitre(query: String): LiveData<List<RendezVous>>

    @Query("SELECT * FROM rendez_vous WHERE statut = :statut ORDER BY dateHeure DESC")
    fun getByStatut(statut: String): LiveData<List<RendezVous>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rdv: RendezVous): Long

    @Update
    suspend fun update(rdv: RendezVous)

    @Delete
    suspend fun delete(rdv: RendezVous)

    @Query("DELETE FROM rendez_vous WHERE id = :id")
    suspend fun deleteById(id: Long)
}
