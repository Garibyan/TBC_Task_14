package com.garibyan.armen.tbc_task_14

import com.garibyan.armen.tbc_task_14.network.ApiService
import com.garibyan.armen.tbc_task_14.network.NewsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val apiService: ApiService) {

    suspend fun getData(): Flow<NewsList> {
        return flow {
            val newsList = apiService.getData()
            emit(newsList)
        }.flowOn(Dispatchers.IO)
    }

}