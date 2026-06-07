package com.example.gestionrendez_vous.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.User
import com.example.gestionrendez_vous.data.local.UserDao
import kotlinx.coroutines.launch

class RegisterViewModel(private val userDao: UserDao) : ViewModel() {

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    fun register(nom: String, email: String, passwordHash: String) {
        viewModelScope.launch {
            try {
                val existingUser = userDao.getByEmail(email)
                if (existingUser == null) {
                    val user = User(nom = nom, email = email, passwordHash = passwordHash)
                    userDao.insert(user)
                    _registerResult.postValue(true)
                } else {
                    _registerResult.postValue(false) // Email déjà utilisé
                }
            } catch (e: Exception) {
                _registerResult.postValue(false)
            }
        }
    }
}
