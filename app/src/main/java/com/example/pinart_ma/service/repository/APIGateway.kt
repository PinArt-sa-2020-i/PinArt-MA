package com.example.pinart_ma.service.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface APIGateway {

    @POST("/graphql/")
    fun registerUser(@Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun authenticateUser(@Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun getMultimediaById(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun getFeedUsers(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun getFeedTags(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun getAllUser(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun getUserById(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("/graphql/")
    fun default(@Header("Authorization") token:String?, @Body jsonBody: JsonObject)  : Call<JsonObject>

}