package com.garibyan.armen.tbc_task_14

import com.garibyan.armen.tbc_task_14.network.ApiService
import com.garibyan.armen.tbc_task_14.network.News
import com.garibyan.armen.tbc_task_14.network.NewsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class Repository(private val apiService: ApiService) {

    suspend fun getData(): Flow<List<News>> {
        return flow {
            val newsList = apiService.getData().content
            emit(newsList)
        }.flowOn(Dispatchers.IO)
    }

}