package com.app.listdatawithapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{controller}")
    suspend fun data(
        @Path(value = "controller", encoded = true) controller: String?
    ): Response<ArrayList<DataList>>
}
