package com.example.pinart_ma.service.repository

import com.example.pinart_ma.service.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface BucketMS {

    @POST("/api/S3Bucket/AddFile")
    fun default( @Body jsonBody: JsonObject)  : Call<JsonObject>

    @Multipart
    @POST("/api/S3Bucket/AddFile")
    fun addImagen( @Part file: MultipartBody.Part?): Call<JsonObject>

}