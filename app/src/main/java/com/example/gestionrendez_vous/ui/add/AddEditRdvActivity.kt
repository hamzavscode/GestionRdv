package com.example.gestionrendez_vous.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gestionrendez_vous.R
import com.example.gestionrendez_vous.api.RetrofitClient
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.data.local.RendezVous
import com.example.gestionrendez_vous.databinding.ActivityAddEditRdvBinding
import com.example.gestionrendez_vous.utils.ImageHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditRdvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditRdvBinding
    private lateinit var viewModel: AddEditRdvViewModel

    private var isEditMode = false
    private var rdvIdToEdit: Long = -1

    private val calendar = Calendar.getInstance()
    private var currentPhotoPath: String? = null
    private var selectedImageUri: Uri? = null
    private var tempPhotoUri: Uri? = null

    // Contrat pour prendre une photo avec la caméra
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            selectedImageUri = tempPhotoUri
            currentPhotoPath = ImageHelper.copyUriToFile(this, tempPhotoUri!!)
            displayImage(currentPhotoPath)
        }
    }

    // Contrat pour choisir une image de la galerie
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            currentPhotoPath = ImageHelper.copyUriToFile(this, it)
            displayImage(currentPhotoPath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditRdvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val database = AppDatabase.getInstance(this)
        val repository = RdvRepository(database.rendezVousDao(), RetrofitClient.instance, this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddEditRdvViewModel(repository) as T
            }
        })[AddEditRdvViewModel::class.java]

        checkEditMode()
        setupListeners()
        observeViewModel()
        updateDateTimeViews()
    }

    private fun checkEditMode() {
        if (intent.hasExtra("RDV_ID")) {
            isEditMode = true
            rdvIdToEdit = intent.getLongExtra("RDV_ID", -1)
            supportActionBar?.title = "Modifier Rendez-vous"
            viewModel.loadRdv(rdvIdToEdit)
        }
    }

    private fun setupListeners() {
        binding.etDate.setOnClickListener { showDatePicker() }
        binding.etTime.setOnClickListener { showTimePicker() }

        binding.btnCamera.setOnClickListener {
            val photoFile = ImageHelper.createImageFile(this)
            tempPhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            takePictureLauncher.launch(tempPhotoUri)
        }

        binding.btnGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener { saveRdv() }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateTimeViews()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeViews()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateDateTimeViews() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.etDate.setText(dateFormat.format(calendar.time))
        binding.etTime.setText(timeFormat.format(calendar.time))
    }

    private fun displayImage(path: String?) {
        if (!path.isNullOrEmpty()) {
            Glide.with(this).load(path).centerCrop().into(binding.ivPreview)
        }
    }

    private fun observeViewModel() {
        viewModel.currentRdv.observe(this) { rdv ->
            rdv?.let {
                binding.etTitre.setText(it.titre)
                binding.etDesc.setText(it.description)
                binding.etLieu.setText(it.lieu)
                calendar.timeInMillis = it.dateHeure
                updateDateTimeViews()
                
                currentPhotoPath = it.imagePath
                displayImage(currentPhotoPath)

                when (it.statut) {
                    "confirme" -> binding.chipAddConfirmed.isChecked = true
                    "annule" -> binding.chipAddCanceled.isChecked = true
                    else -> binding.chipAddPending.isChecked = true
                }
            }
        }

        viewModel.saveStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Rendez-vous sauvegardé", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveRdv() {
        val titre = binding.etTitre.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()
        val lieu = binding.etLieu.text.toString().trim()

        if (titre.isEmpty()) {
            binding.etTitreLayout.error = "Le titre est requis"
            return
        }

        val statut = when (binding.chipGroupAddStatus.checkedChipId) {
            R.id.chipAddConfirmed -> "confirme"
            R.id.chipAddCanceled -> "annule"
            else -> "en_attente"
        }

        val rdv = RendezVous(
            id = if (isEditMode) rdvIdToEdit else 0,
            titre = titre,
            description = desc,
            dateHeure = calendar.timeInMillis,
            lieu = lieu,
            imagePath = currentPhotoPath,
            statut = statut
        )

        viewModel.saveRdv(rdv, isEditMode)
    }
}
