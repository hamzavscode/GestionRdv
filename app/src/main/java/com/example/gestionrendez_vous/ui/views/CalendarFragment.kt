package com.example.gestionrendez_vous.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionrendez_vous.MainApplication
import com.example.gestionrendez_vous.databinding.FragmentCalendarBinding
import com.example.gestionrendez_vous.ui.viewmodels.CalendarViewModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalendarViewModel
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (requireActivity().application as MainApplication).container

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CalendarViewModel(appContainer.appDao) as T
            }
        })[CalendarViewModel::class.java]

        adapter = AppointmentAdapter()
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = adapter

        // Load today's appointments by default
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModel.loadAppointmentsByDate(today)

        // Calendar date change listener
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            binding.tvSelectedDate.text = formatDateLabel(year, month, dayOfMonth)
            viewModel.loadAppointmentsByDate(selectedDate)
        }

        // FAB to add appointment
        binding.fabAddAppointment.setOnClickListener {
            startActivity(Intent(requireContext(), AddAppointmentActivity::class.java))
        }

        // Observe
        viewModel.appointments.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun formatDateLabel(year: Int, month: Int, day: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        val sdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        return sdf.format(cal.time)
    }

    override fun onResume() {
        super.onResume()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModel.loadAppointmentsByDate(today)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
