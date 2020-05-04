package com.example.pinart_ma.service.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIMultimedia {

    @POST("/multimedia/add")
    fun addMultimedia(@Body jsonBody: JsonObject)  : Call<JsonObject>
}