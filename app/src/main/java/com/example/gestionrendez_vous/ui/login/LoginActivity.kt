package com.example.gestionrendez_vous.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionrendez_vous.MainActivity
import com.example.gestionrendez_vous.databinding.ActivityLoginBinding
import com.example.gestionrendez_vous.security.SecurePreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var securePreferences: SecurePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        securePreferences = SecurePreferences(this)

        // Check if already logged in
        if (securePreferences.getToken() != null) {
            navigateToMain()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                // Simulate API call for login
                val mockToken = "mock_token_12345"
                securePreferences.saveToken(mockToken)
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailLayout.error = "Email invalide"
            isValid = false
        } else {
            binding.etEmailLayout.error = null
        }

        if (password.isEmpty() || password.length < 6) {
            binding.etPasswordLayout.error = "Le mot de passe doit contenir au moins 6 caractères"
            isValid = false
        } else {
            binding.etPasswordLayout.error = null
        }

        return isValid
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
