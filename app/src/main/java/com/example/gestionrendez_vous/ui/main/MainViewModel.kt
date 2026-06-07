package com.example.gestionrendez_vous.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.RendezVous
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RdvRepository) : ViewModel() {

    var allRendezVous: LiveData<List<RendezVous>> = repository.getAllRendezVous()
    
    private val _syncStatus = MutableLiveData<Boolean>()
    val syncStatus: LiveData<Boolean> = _syncStatus

    init {
        syncData()
    }

    fun syncData() {
        viewModelScope.launch {
            try {
                repository.syncFromApi()
                _syncStatus.postValue(true)
            } catch (e: Exception) {
                _syncStatus.postValue(false)
            }
        }
    }

    fun search(query: String) {
        allRendezVous = if (query.isEmpty()) {
            repository.getAllRendezVous()
        } else {
            repository.searchRendezVous(query)
        }
    }

    fun filterByStatut(statut: String?) {
        allRendezVous = if (statut == null) {
            repository.getAllRendezVous()
        } else {
            repository.getRendezVousByStatut(statut)
        }
    }
    
    fun deleteRdv(rdv: RendezVous) {
        viewModelScope.launch {
            repository.delete(rdv)
        }
    }
}
