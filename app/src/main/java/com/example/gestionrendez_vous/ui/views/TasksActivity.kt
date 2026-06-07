package com.example.gestionrendez_vous.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionrendez_vous.MainApplication
import com.example.gestionrendez_vous.databinding.ActivityTasksBinding
import com.example.gestionrendez_vous.ui.viewmodels.TasksViewModel

class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private lateinit var viewModel: TasksViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val container = (application as MainApplication).container
        
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TasksViewModel(container.appDao, container.apiService) as T
            }
        })[TasksViewModel::class.java]

        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        // Initial fetch
        viewModel.fetchTasksFromApi()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter { task, isChecked ->
            viewModel.toggleTaskCompletion(task, isChecked)
        }
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(this@TasksActivity)
            adapter = taskAdapter
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish() // Return to dashboard
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchTasksFromApi()
        }

        binding.fabAdd.setOnClickListener {
            Toast.makeText(this, "Add new local task (Phase 4)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }
}
