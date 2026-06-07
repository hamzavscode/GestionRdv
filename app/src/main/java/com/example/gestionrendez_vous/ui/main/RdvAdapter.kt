package com.example.gestionrendez_vous.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionrendez_vous.R
import com.example.gestionrendez_vous.api.Rdv

class RdvAdapter(private val items: List<Rdv>) : RecyclerView.Adapter<RdvAdapter.RdvViewHolder>() {

    class RdvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val ivStatus: ImageView = view.findViewById(R.id.ivStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RdvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rdv, parent, false)
        return RdvViewHolder(view)
    }

    override fun onBindViewHolder(holder: RdvViewHolder, position: Int) {
        val rdv = items[position]
        holder.tvTitle.text = rdv.title
        holder.tvDetails.text = "Détails (User ID: ${rdv.userId})"
        
        if (rdv.completed) {
            holder.ivStatus.setImageResource(android.R.drawable.checkbox_on_background)
        } else {
            holder.ivStatus.setImageResource(android.R.drawable.checkbox_off_background)
        }
    }

    override fun getItemCount() = items.size
}
