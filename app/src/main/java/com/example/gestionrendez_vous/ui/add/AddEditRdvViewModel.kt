package com.example.gestionrendez_vous.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.RendezVous
import kotlinx.coroutines.launch

class AddEditRdvViewModel(private val repository: RdvRepository) : ViewModel() {

    private val _saveStatus = MutableLiveData<Boolean>()
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _currentRdv = MutableLiveData<RendezVous?>()
    val currentRdv: LiveData<RendezVous?> = _currentRdv

    fun loadRdv(id: Long) {
        viewModelScope.launch {
            val rdv = repository.getRendezVousById(id)
            _currentRdv.postValue(rdv)
        }
    }

    fun saveRdv(rdv: RendezVous, isEditMode: Boolean) {
        viewModelScope.launch {
            try {
                if (isEditMode) {
                    repository.update(rdv)
                } else {
                    repository.insert(rdv)
                }
                _saveStatus.postValue(true)
            } catch (e: Exception) {
                _saveStatus.postValue(false)
            }
        }
    }
}
