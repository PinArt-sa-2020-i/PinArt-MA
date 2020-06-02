package com.example.pinart_ma.service.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface FCM {


    @POST("")
    fun defaultRequest(
        @Url() url: String?,
        @Header("Content-Type") contentType: String?,
        @Header("Authorization") key:String?,
        @Header("Content-Length") contentLength:String?,
        @Body jsonBody: JsonObject)  : Call<JsonObject>

    @POST("")
    fun defaultRequest2(
        @Url() url: String?,
        @Header("Content-Type") contentType: String?,
        @Header("Authorization") key:String?,
        @Body jsonBody: JsonObject)  : Call<JsonObject>


    @DELETE("")
    fun unsuscriberTopic(
        @Url() url: String?,
        @Header("Content-Type") contentType: String?,
        @Header("Authorization") key:String?)  : Call<JsonObject>

    @GET("")
    fun getTopicsSuscriber(
        @Url() url: String?,
        @Header("Content-Type") contentType: String?,
        @Header("Authorization") key:String?)  : Call<JsonObject>
}