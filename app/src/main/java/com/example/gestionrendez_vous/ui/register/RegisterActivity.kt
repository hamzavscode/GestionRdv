package com.example.gestionrendez_vous.ui.register

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.databinding.ActivityRegisterBinding
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getInstance(this).userDao()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(userDao) as T
            }
        })[RegisterViewModel::class.java]

        viewModel.registerResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Compte créé avec succès ! Connectez-vous.", Toast.LENGTH_LONG).show()
                finish() // Retour au login
            } else {
                Toast.makeText(this, "Erreur : cet email est peut-être déjà utilisé.", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val nom = binding.etNom.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etPasswordConfirm.text.toString()

            if (validate(nom, email, password, confirm)) {
                viewModel.register(nom, email, hashPassword(password))
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            finish()
        }
    }

    private fun validate(nom: String, email: String, pass: String, conf: String): Boolean {
        if (nom.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailLayout.error = "Email invalide"
            return false
        } else {
            binding.etEmailLayout.error = null
        }
        if (pass.length < 6) {
            binding.etPasswordLayout.error = "Min 6 caractères"
            return false
        } else {
            binding.etPasswordLayout.error = null
        }
        if (pass != conf) {
            binding.etPasswordConfirmLayout.error = "Les mots de passe ne correspondent pas"
            return false
        } else {
            binding.etPasswordConfirmLayout.error = null
        }
        return true
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
