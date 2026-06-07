package com.example.gestionrendez_vous.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.AppDao
import com.example.gestionrendez_vous.data.models.Appointment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CalendarViewModel(private val appDao: AppDao) : ViewModel() {

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    fun loadAppointmentsByDate(date: String) {
        viewModelScope.launch {
            appDao.getAppointmentsByDate(date).collectLatest { list ->
                _appointments.postValue(list)
            }
        }
    }

    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appDao.insertAppointment(appointment)
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appDao.deleteAppointment(appointment)
        }
    }
}
