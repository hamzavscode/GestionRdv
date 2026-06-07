package com.example.gestionrendez_vous

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionrendez_vous.api.RetrofitClient
import com.example.gestionrendez_vous.data.RdvRepository
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.databinding.ActivityMainBinding
import com.example.gestionrendez_vous.ui.add.AddEditRdvActivity
import com.example.gestionrendez_vous.ui.detail.DetailRdvActivity
import com.example.gestionrendez_vous.ui.login.LoginActivity
import com.example.gestionrendez_vous.ui.main.MainViewModel
import com.example.gestionrendez_vous.ui.main.RdvAdapter
import com.example.gestionrendez_vous.utils.NetworkHelper
import com.example.gestionrendez_vous.utils.SessionManager
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RdvAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        sessionManager = SessionManager(this)

        val database = AppDatabase.getInstance(this)
        val repository = RdvRepository(database.rendezVousDao(), RetrofitClient.instance, this)
        
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        // Indicateur Offline/Online
        if (!NetworkHelper.isOnline(this)) {
            Snackbar.make(binding.root, "Mode hors-ligne actif", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = RdvAdapter { rdv ->
            val intent = Intent(this, DetailRdvActivity::class.java)
            intent.putExtra("RDV_ID", rdv.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAddRdv.setOnClickListener {
            startActivity(Intent(this, AddEditRdvActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                observeViewModel() // Re-attach observer to new LiveData
                return true
            }
        })

        binding.chipGroupStatus.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            val status = when (checkedIds[0]) {
                R.id.chipPending -> "en_attente"
                R.id.chipConfirmed -> "confirme"
                R.id.chipCanceled -> "annule"
                else -> null
            }
            viewModel.filterByStatut(status)
            observeViewModel()
        }
    }

    private fun observeViewModel() {
        // Obtenir la LiveData actuelle (qui change en cas de recherche/filtre)
        viewModel.allRendezVous.removeObservers(this)
        viewModel.allRendezVous.observe(this) { rdvList ->
            adapter.submitList(rdvList)
            if (rdvList.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                sessionManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
