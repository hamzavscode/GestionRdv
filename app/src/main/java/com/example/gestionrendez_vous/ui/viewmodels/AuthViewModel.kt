package com.example.gestionrendez_vous.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.AppDao
import com.example.gestionrendez_vous.data.models.User
import com.example.gestionrendez_vous.utils.SessionManager
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthViewModel(
    private val appDao: AppDao,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val user = appDao.getUserByEmail(email)
                if (user != null && user.passwordHash == hashPassword(pass)) {
                    sessionManager.createLoginSession(user.email, user.name)
                    _loginStatus.postValue(true)
                } else {
                    _errorMessage.postValue("Invalid email or password.")
                    _loginStatus.postValue(false)
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred.")
                _loginStatus.postValue(false)
            }
        }
    }

    fun register(name: String, email: String, pass: String) {
        viewModelScope.launch {
            try {
                val existingUser = appDao.getUserByEmail(email)
                if (existingUser == null) {
                    val user = User(
                        name = name,
                        email = email,
                        passwordHash = hashPassword(pass),
                        role = "Service Provider"
                    )
                    appDao.insertUser(user)
                    _registerStatus.postValue(true)
                } else {
                    _errorMessage.postValue("Email already in use.")
                    _registerStatus.postValue(false)
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred during registration.")
                _registerStatus.postValue(false)
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
