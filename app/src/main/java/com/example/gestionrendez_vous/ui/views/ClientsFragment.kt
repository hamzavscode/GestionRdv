package com.example.gestionrendez_vous.ui.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionrendez_vous.MainApplication
import com.example.gestionrendez_vous.data.models.Client
import com.example.gestionrendez_vous.databinding.FragmentClientsBinding
import com.example.gestionrendez_vous.ui.viewmodels.ClientsViewModel

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ClientsViewModel
    private lateinit var adapter: ClientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (requireActivity().application as MainApplication).container

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ClientsViewModel(appContainer.appDao) as T
            }
        })[ClientsViewModel::class.java]

        adapter = ClientAdapter()
        binding.rvClients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClients.adapter = adapter

        // Observe all clients
        viewModel.allClients.observe(viewLifecycleOwner) { clients ->
            adapter.submitList(clients)
            binding.tvClientCount.text = "${clients.size} Total"
            viewModel.searchClients(binding.etSearch.text.toString(), clients)
        }

        // Observe filtered clients
        viewModel.filteredClients.observe(viewLifecycleOwner) { filtered ->
            adapter.submitList(filtered)
        }

        // Search
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.allClients.value?.let { all ->
                    viewModel.searchClients(s.toString(), all)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // FAB - Add Client Dialog
        binding.fabAddClient.setOnClickListener {
            showAddClientDialog()
        }
    }

    private fun showAddClientDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(
            android.R.layout.simple_list_item_1, null
        )

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Add Client")
            .setMessage("Enter client details:")

        val nameInput = android.widget.EditText(requireContext()).apply { hint = "Full Name" }
        val phoneInput = android.widget.EditText(requireContext()).apply { hint = "Phone Number"; inputType = android.text.InputType.TYPE_CLASS_PHONE }
        val emailInput = android.widget.EditText(requireContext()).apply { hint = "Email Address"; inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS }

        val layout = android.widget.LinearLayout(requireContext()).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
            addView(nameInput)
            addView(phoneInput)
            addView(emailInput)
        }

        builder.setView(layout)
        builder.setPositiveButton("Save") { _, _ ->
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                viewModel.addClient(Client(name = name, phone = phone, email = email))
                Toast.makeText(requireContext(), "Client added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Name and phone are required", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
