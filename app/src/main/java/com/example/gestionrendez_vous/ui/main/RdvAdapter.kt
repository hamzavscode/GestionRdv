package com.example.gestionrendez_vous.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gestionrendez_vous.R
import com.example.gestionrendez_vous.data.local.RendezVous
import com.example.gestionrendez_vous.databinding.ItemRdvBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RdvAdapter(private val onItemClick: (RendezVous) -> Unit) :
    ListAdapter<RendezVous, RdvAdapter.RdvViewHolder>(RdvDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RdvViewHolder {
        val binding = ItemRdvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RdvViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RdvViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RdvViewHolder(
        private val binding: ItemRdvBinding,
        private val onItemClick: (RendezVous) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rdv: RendezVous) {
            binding.root.setOnClickListener { onItemClick(rdv) }

            binding.tvTitle.text = rdv.titre
            binding.tvLocation.text = rdv.lieu

            // Format date
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(rdv.dateHeure))

            // Load Image with Glide
            if (!rdv.imagePath.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(rdv.imagePath)
                    .centerCrop()
                    .into(binding.ivRdvImage)
            } else {
                binding.ivRdvImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            // Status chip colors
            when (rdv.statut) {
                "confirme" -> {
                    binding.chipStatus.text = "Confirmé"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_confirme))
                    binding.chipStatus.setChipBackgroundColorResource(R.color.status_confirme_bg)
                }
                "annule" -> {
                    binding.chipStatus.text = "Annulé"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_annule))
                    binding.chipStatus.setChipBackgroundColorResource(R.color.status_annule_bg)
                }
                else -> {
                    binding.chipStatus.text = "En attente"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_en_attente))
                    binding.chipStatus.setChipBackgroundColorResource(R.color.status_en_attente_bg)
                }
            }
        }
    }

    class RdvDiffCallback : DiffUtil.ItemCallback<RendezVous>() {
        override fun areItemsTheSame(oldItem: RendezVous, newItem: RendezVous): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RendezVous, newItem: RendezVous): Boolean {
            return oldItem == newItem
        }
    }
}
