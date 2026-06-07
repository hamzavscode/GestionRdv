package com.example.gestionrendez_vous.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gestionrendez_vous.MainApplication
import com.example.gestionrendez_vous.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val sessionManager = (requireActivity().application as MainApplication).container.sessionManager
        val userName = sessionManager.getUserEmail()?.substringBefore("@")?.capitalize() ?: "User"
        
        binding.tvGreeting.text = "Good Morning, $userName"

        binding.btnGoToTasks.setOnClickListener {
            // Navigate to TasksActivity
            requireActivity().startActivity(android.content.Intent(requireContext(), TasksActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
