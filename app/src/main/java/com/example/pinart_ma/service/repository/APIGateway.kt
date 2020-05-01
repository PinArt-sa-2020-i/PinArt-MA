package com.example.pinart_ma.service.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIGateway {

    @POST("/")
    fun registerUser(@Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/")
    fun authenticateUser(@Body jsonBody: JsonObject)  : Call<JsonObject>

}