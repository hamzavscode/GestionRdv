package com.example.gestionrendez_vous

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionrendez_vous.api.Rdv
import com.example.gestionrendez_vous.api.RetrofitClient
import com.example.gestionrendez_vous.databinding.ActivityMainBinding
import com.example.gestionrendez_vous.ui.main.RdvAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        fetchRendezVous()
    }

    private fun fetchRendezVous() {
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getRendezVous().enqueue(object : Callback<List<Rdv>> {
            override fun onResponse(call: Call<List<Rdv>>, response: Response<List<Rdv>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val rdvList = response.body()!!
                    val adapter = RdvAdapter(rdvList)
                    binding.recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@MainActivity, "Erreur API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Rdv>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Erreur Réseau: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
