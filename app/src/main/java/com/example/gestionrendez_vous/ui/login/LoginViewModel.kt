package com.example.gestionrendez_vous.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.UserDao
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun login(email: String, passwordHash: String) {
        viewModelScope.launch {
            val user = userDao.getByEmail(email)
            if (user != null && user.passwordHash == passwordHash) {
                _loginResult.postValue(true)
            } else {
                _loginResult.postValue(false)
            }
        }
    }
}
