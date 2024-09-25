package com.app.listdatawithapi


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun data(): Result<ArrayList<DataList>> {
        try {

            val response = apiService.data("photos")
            return if (response.isSuccessful) {
                Result.success(response.body() ?: ArrayList<DataList>() )
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}