package com.example.gestionrendez_vous.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.RendezVous
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RdvRepository) : ViewModel() {

    private val _rdv = MutableLiveData<RendezVous?>()
    val rdv: LiveData<RendezVous?> = _rdv

    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> = _deleteStatus

    fun loadRdv(id: Long) {
        viewModelScope.launch {
            val loaded = repository.getRendezVousById(id)
            _rdv.postValue(loaded)
        }
    }

    fun deleteRdv() {
        viewModelScope.launch {
            _rdv.value?.let {
                repository.delete(it)
                _deleteStatus.postValue(true)
            }
        }
    }
}
