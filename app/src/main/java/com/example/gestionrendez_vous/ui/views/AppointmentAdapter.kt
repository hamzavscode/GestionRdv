package com.example.gestionrendez_vous.ui.views

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionrendez_vous.data.models.Appointment
import com.example.gestionrendez_vous.databinding.ItemAppointmentBinding

class AppointmentAdapter : ListAdapter<Appointment, AppointmentAdapter.ViewHolder>(AppointmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: Appointment) {
            binding.tvClientName.text = appointment.clientName
            binding.tvTime.text = "${appointment.time} - ${appointment.serviceType}"

            // Avatar initials
            val initials = appointment.clientName.split(" ").take(2).joinToString("") {
                it.first().uppercase()
            }
            binding.tvAvatar.text = initials

            // Status badge colors
            when (appointment.status) {
                "Confirmed" -> {
                    binding.tvStatusBadge.text = "Confirmed"
                    binding.tvStatusBadge.setTextColor(Color.parseColor("#388E3C"))
                    binding.tvStatusBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8F5E9"))
                    binding.statusBorder.setBackgroundColor(Color.parseColor("#3DDC84"))
                }
                "Pending" -> {
                    binding.tvStatusBadge.text = "Pending"
                    binding.tvStatusBadge.setTextColor(Color.parseColor("#E65100"))
                    binding.tvStatusBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFF3E0"))
                    binding.statusBorder.setBackgroundColor(Color.parseColor("#FF9800"))
                }
                else -> {
                    binding.tvStatusBadge.text = "Cancelled"
                    binding.tvStatusBadge.setTextColor(Color.parseColor("#C62828"))
                    binding.tvStatusBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEBEE"))
                    binding.statusBorder.setBackgroundColor(Color.parseColor("#EF5350"))
                }
            }
        }
    }
}

class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>() {
    override fun areItemsTheSame(a: Appointment, b: Appointment) = a.id == b.id
    override fun areContentsTheSame(a: Appointment, b: Appointment) = a == b
}
