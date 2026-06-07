package com.example.gestionrendez_vous.ui.views

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionrendez_vous.data.models.Task
import com.example.gestionrendez_vous.databinding.ItemTaskBinding

class TaskAdapter(
    private val onTaskCheckChanged: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.cbTask.setOnCheckedChangeListener(null)
            binding.cbTask.isChecked = task.isCompleted
            
            binding.cbTask.setOnCheckedChangeListener { _, isChecked ->
                onTaskCheckChanged(task, isChecked)
            }

            binding.tvPriorityBadge.text = task.priority
            
            // Set colors based on priority
            val colorRes = when (task.priority) {
                "High" -> Color.parseColor("#D32F2F") // Red
                "Medium" -> Color.parseColor("#1976D2") // Blue
                else -> Color.parseColor("#388E3C") // Green
            }
            val bgColorRes = when (task.priority) {
                "High" -> Color.parseColor("#FFEBEE")
                "Medium" -> Color.parseColor("#E3F2FD")
                else -> Color.parseColor("#E8F5E9")
            }

            binding.priorityBorder.setBackgroundColor(colorRes)
            binding.tvPriorityBadge.setTextColor(colorRes)
            binding.tvPriorityBadge.backgroundTintList = ColorStateList.valueOf(bgColorRes)
            
            if (task.isCompleted) {
                binding.tvTaskTitle.alpha = 0.5f
            } else {
                binding.tvTaskTitle.alpha = 1.0f
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
