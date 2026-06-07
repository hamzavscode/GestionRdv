package com.example.gestionrendez_vous.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.AppDao
import com.example.gestionrendez_vous.data.models.Client
import kotlinx.coroutines.launch

class ClientsViewModel(private val appDao: AppDao) : ViewModel() {

    val allClients: LiveData<List<Client>> = appDao.getAllClients().asLiveData()

    private val _filteredClients = MutableLiveData<List<Client>>()
    val filteredClients: LiveData<List<Client>> = _filteredClients

    fun searchClients(query: String, allList: List<Client>) {
        if (query.isBlank()) {
            _filteredClients.value = allList
        } else {
            _filteredClients.value = allList.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.phone.contains(query) ||
                it.email.contains(query, ignoreCase = true)
            }
        }
    }

    fun addClient(client: Client) {
        viewModelScope.launch {
            appDao.insertClient(client)
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            appDao.deleteClient(client)
        }
    }
}
