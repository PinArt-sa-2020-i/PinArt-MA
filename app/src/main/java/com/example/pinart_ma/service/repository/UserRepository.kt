package com.example.pinart_ma.service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    var URL: String = "http://ec2-54-92-134-33.compute-1.amazonaws.com:5000"

    companion object {
        @Volatile private var userRepository: UserRepository? = null

        fun getInstance() =
            userRepository?: synchronized(this) {
                userRepository ?: UserRepository().also { userRepository  = it }
            }
    }


    fun registerUser(firstName: String, lastName: String, username: String, password: String): MutableLiveData<Int>{
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "mutation{ registerUser(register: { firstName: \"$firstName\" lastName: \"$lastName\" username: \"$username\" password: \"$password\"}){ id } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.registerUser(gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = -1
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                var data: JsonObject = response?.body()?.get("data") as JsonObject

                if(data.get("registerUser").toString() == "null"){
                    liveData.value = -1
                }
                else {
                    liveData.value = data.getAsJsonObject("registerUser").get("id").asInt
                }
            }
        })
        return liveData
    }

}