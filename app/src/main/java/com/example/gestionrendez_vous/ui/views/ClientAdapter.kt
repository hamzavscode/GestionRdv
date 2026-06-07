package com.example.gestionrendez_vous.ui.views

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionrendez_vous.data.models.Client
import com.example.gestionrendez_vous.databinding.ItemClientBinding

class ClientAdapter : ListAdapter<Client, ClientAdapter.ViewHolder>(ClientDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Client) {
            binding.tvName.text = client.name
            binding.tvPhone.text = client.phone

            val initials = client.name.split(" ").take(2).joinToString("") {
                it.first().uppercase()
            }
            binding.tvAvatar.text = initials

            binding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${client.phone}"))
                it.context.startActivity(intent)
            }

            binding.btnMessage.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${client.phone}"))
                it.context.startActivity(intent)
            }
        }
    }
}

class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(a: Client, b: Client) = a.id == b.id
    override fun areContentsTheSame(a: Client, b: Client) = a == b
}
