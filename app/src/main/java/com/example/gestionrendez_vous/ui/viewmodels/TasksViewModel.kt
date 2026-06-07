package com.example.gestionrendez_vous.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionrendez_vous.data.local.AppDao
import com.example.gestionrendez_vous.data.models.Task
import com.example.gestionrendez_vous.data.remote.ApiService
import kotlinx.coroutines.launch

class TasksViewModel(
    private val appDao: AppDao,
    private val apiService: ApiService
) : ViewModel() {

    // Offline-first approach: Observe from Room database
    val tasks: LiveData<List<Task>> = appDao.getAllTasks().asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchTasksFromApi() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Fetch from JSONPlaceholder
                val apiTasks = apiService.getTodos()
                
                // Map to our local Task model and assign random priorities/dates for UI demonstration
                val localTasks = apiTasks.take(20).mapIndexed { index, apiTask ->
                    val priority = when {
                        index % 3 == 0 -> "High"
                        index % 3 == 1 -> "Medium"
                        else -> "Low"
                    }
                    
                    Task(
                        id = apiTask.id,
                        title = apiTask.title.capitalize(),
                        priority = priority,
                        isCompleted = apiTask.completed
                    )
                }
                
                // Save to Room (Local DB)
                appDao.insertTasks(localTasks)
                
            } catch (e: Exception) {
                Log.e("TasksViewModel", "Error fetching API", e)
                _error.value = "Failed to sync with API. Showing offline data."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleTaskCompletion(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = isChecked)
            appDao.updateTask(updatedTask)
        }
    }
}
