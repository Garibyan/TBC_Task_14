package com.garibyan.armen.tbc_task_14.viewModels

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_task_14.Repository
import com.garibyan.armen.tbc_task_14.network.ApiClient
import com.garibyan.armen.tbc_task_14.network.News
import com.garibyan.armen.tbc_task_14.screens.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
    = Repository(ApiClient.apiService)
) : ViewModel() {

    private val _newsState = MutableStateFlow<ScreenState<List<News>>>(ScreenState.Loading)
    val newsState = _newsState.asStateFlow()

    init {
        getNews()
    }

    fun getNews() {
        viewModelScope.launch {
            _newsState.value = ScreenState.Loading
            try {
                repository.getData().collect {
                    _newsState.value = ScreenState.Success(it)
                    d("RESPONSE_MODEL", it.toString())
                }
            } catch (e: Exception) {
                _newsState.value = ScreenState.Error(e.toString())
            }
        }
    }
}