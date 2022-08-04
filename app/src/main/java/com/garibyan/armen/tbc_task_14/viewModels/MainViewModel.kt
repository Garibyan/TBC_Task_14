package com.garibyan.armen.tbc_task_14.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_task_14.Repository
import com.garibyan.armen.tbc_task_14.network.ApiClient
import com.garibyan.armen.tbc_task_14.network.NewsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
    = Repository(ApiClient.apiService)
) : ViewModel() {

    private val _modelState = MutableStateFlow<ModelState>(ModelState.Loading)
    val modelState: StateFlow<ModelState> = _modelState

    sealed class ModelState {
        data class Success(val newsList: NewsList) : ModelState()
        data class Error(val error: String) : ModelState()
        object Loading : ModelState()
    }

    init {
        getNews()
    }

    fun getNews() = viewModelScope.launch {
        _modelState.value = ModelState.Loading
        try {
            repository.getData().collect {
                _modelState.value = ModelState.Success(it)
                Log.d("RESPONSE_MODEL", it.toString())
            }
        } catch (e: Exception) {
            _modelState.value = ModelState.Error(e.toString())
        }
    }
}