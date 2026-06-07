package com.example.gestionrendez_vous.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gestionrendez_vous.R
import com.example.gestionrendez_vous.api.RetrofitClient
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.databinding.ActivityDetailRdvBinding
import com.example.gestionrendez_vous.ui.add.AddEditRdvActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailRdvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRdvBinding
    private lateinit var viewModel: DetailViewModel
    private var rdvId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRdvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "" // Titre géré par CollapsingToolbar
        binding.toolbar.setNavigationOnClickListener { finish() }

        rdvId = intent.getLongExtra("RDV_ID", -1)
        if (rdvId == -1L) {
            finish()
            return
        }

        val database = AppDatabase.getInstance(this)
        val repository = RdvRepository(database.rendezVousDao(), RetrofitClient.instance, this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(repository) as T
            }
        })[DetailViewModel::class.java]

        setupListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRdv(rdvId) // Recharge au cas où on revient d'une modif
    }

    private fun setupListeners() {
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditRdvActivity::class.java)
            intent.putExtra("RDV_ID", rdvId)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Supprimer le rendez-vous")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce rendez-vous ?")
                .setPositiveButton("Oui") { _, _ -> viewModel.deleteRdv() }
                .setNegativeButton("Non", null)
                .show()
        }
    }

    private fun observeViewModel() {
        viewModel.rdv.observe(this) { rdv ->
            rdv?.let {
                binding.tvDetailTitle.text = it.titre
                binding.tvDetailDesc.text = it.description
                binding.tvDetailLieu.text = it.lieu

                val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
                binding.tvDetailDate.text = sdf.format(Date(it.dateHeure))

                if (!it.imagePath.isNullOrEmpty()) {
                    Glide.with(this).load(it.imagePath).centerCrop().into(binding.ivDetailHero)
                } else {
                    binding.ivDetailHero.setImageResource(android.R.drawable.ic_menu_gallery)
                }

                when (it.statut) {
                    "confirme" -> {
                        binding.chipDetailStatus.text = "Confirmé"
                        binding.chipDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_confirme))
                        binding.chipDetailStatus.setChipBackgroundColorResource(R.color.status_confirme_bg)
                    }
                    "annule" -> {
                        binding.chipDetailStatus.text = "Annulé"
                        binding.chipDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_annule))
                        binding.chipDetailStatus.setChipBackgroundColorResource(R.color.status_annule_bg)
                    }
                    else -> {
                        binding.chipDetailStatus.text = "En attente"
                        binding.chipDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_en_attente))
                        binding.chipDetailStatus.setChipBackgroundColorResource(R.color.status_en_attente_bg)
                    }
                }
            }
        }

        viewModel.deleteStatus.observe(this) { deleted ->
            if (deleted) {
                Toast.makeText(this, "Rendez-vous supprimé", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
