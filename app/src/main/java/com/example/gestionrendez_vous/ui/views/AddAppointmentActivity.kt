package com.example.gestionrendez_vous.ui.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionrendez_vous.MainApplication
import com.example.gestionrendez_vous.data.models.Appointment
import com.example.gestionrendez_vous.databinding.ActivityAddAppointmentBinding
import com.example.gestionrendez_vous.ui.viewmodels.CalendarViewModel
import java.util.*

class AddAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAppointmentBinding
    private lateinit var viewModel: CalendarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val container = (application as MainApplication).container

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CalendarViewModel(container.appDao) as T
            }
        })[CalendarViewModel::class.java]

        // Status Spinner
        val statuses = arrayOf("Confirmed", "Pending", "Cancelled")
        binding.spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statuses)

        // Date Picker
        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                binding.etDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time Picker
        binding.etTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, h, m ->
                binding.etTime.setText(String.format("%02d:%02d", h, m))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // Save
        binding.btnSave.setOnClickListener {
            val name = binding.etClientName.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val time = binding.etTime.text.toString().trim()
            val service = binding.etServiceType.text.toString().trim()
            val status = binding.spinnerStatus.selectedItem.toString()

            if (name.isEmpty() || date.isEmpty() || time.isEmpty() || service.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val appointment = Appointment(
                clientName = name,
                date = date,
                time = time,
                serviceType = service,
                status = status
            )
            viewModel.addAppointment(appointment)
            Toast.makeText(this, "Appointment saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
