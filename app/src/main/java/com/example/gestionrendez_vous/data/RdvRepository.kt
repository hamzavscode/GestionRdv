package com.example.gestionrendez_vous.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gestionrendez_vous.api.ApiRdv
import com.example.gestionrendez_vous.api.RdvService
import com.example.gestionrendez_vous.data.local.RendezVous
import com.example.gestionrendez_vous.data.local.RendezVousDao
import com.example.gestionrendez_vous.utils.ImageHelper
import com.example.gestionrendez_vous.utils.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RdvRepository(
    private val rdvDao: RendezVousDao,
    private val rdvService: RdvService,
    private val context: Context
) {

    // 1. Toujours retourner les données locales (Room) comme source de vérité
    fun getAllRendezVous(): LiveData<List<RendezVous>> {
        return rdvDao.getAll()
    }

    fun getRendezVousByStatut(statut: String): LiveData<List<RendezVous>> {
        return rdvDao.getByStatut(statut)
    }

    fun searchRendezVous(query: String): LiveData<List<RendezVous>> {
        return rdvDao.searchByTitre(query)
    }

    suspend fun getRendezVousById(id: Long): RendezVous? {
        return rdvDao.getById(id)
    }

    // 2. Synchroniser avec l'API si le réseau est disponible
    suspend fun syncFromApi() {
        if (NetworkHelper.isOnline(context)) {
            try {
                val apiData = rdvService.getRendezVous()
                // Convertir les données de l'API mock (todos) vers notre modèle local
                // On garde seulement les 10 premiers pour éviter de polluer la base locale
                apiData.take(10).forEach { apiItem ->
                    val statut = if (apiItem.completed) "confirme" else "en_attente"
                    val exists = rdvDao.searchByTitre(apiItem.title) // Simple check, usually done by ID
                    
                    // Dans un vrai projet, on synchroniserait les IDs. Ici on simule :
                    val newRdv = RendezVous(
                        titre = apiItem.title,
                        description = "RDV importé depuis l'API (ID: ${apiItem.id})",
                        dateHeure = System.currentTimeMillis() + 86400000, // Demain
                        lieu = "En ligne",
                        imagePath = null,
                        statut = statut
                    )
                    // Insert seulement si pas déjà présent (logique simplifiée)
                    // rdvDao.insert(newRdv)
                }
            } catch (e: Exception) {
                e.printStackTrace() // Erreur silencieuse de synchronisation
            }
        }
    }

    // 3. Opérations locales
    suspend fun insert(rdv: RendezVous) {
        withContext(Dispatchers.IO) {
            rdvDao.insert(rdv)
            // Optionnel : push to API si online
        }
    }

    suspend fun update(rdv: RendezVous) {
        withContext(Dispatchers.IO) {
            rdvDao.update(rdv)
        }
    }

    suspend fun delete(rdv: RendezVous) {
        withContext(Dispatchers.IO) {
            // Supprimer l'image du stockage local s'il y en a une
            ImageHelper.deleteImage(rdv.imagePath)
            rdvDao.delete(rdv)
        }
    }
}
