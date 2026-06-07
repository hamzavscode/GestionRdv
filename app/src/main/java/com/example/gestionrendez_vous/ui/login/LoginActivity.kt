package com.example.gestionrendez_vous.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionrendez_vous.MainActivity
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.databinding.ActivityLoginBinding
import com.example.gestionrendez_vous.ui.register.RegisterActivity
import com.example.gestionrendez_vous.utils.SessionManager
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Si déjà connecté
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val userDao = AppDatabase.getInstance(this).userDao()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userDao) as T
            }
        })[LoginViewModel::class.java]

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                // Dans un vrai projet, on génèrerait un token JWT
                sessionManager.saveToken("user_token_${binding.etEmail.text.toString()}")
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, hashPassword(password))
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
