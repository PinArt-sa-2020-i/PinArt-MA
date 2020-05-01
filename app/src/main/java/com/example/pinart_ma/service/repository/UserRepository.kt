package com.example.pinart_ma.service.repository

import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.User
import com.google.gson.JsonElement
import com.google.gson.JsonNull
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


    fun registerUser(firstName: String, lastName: String, username: String, password: String, correo: String): MutableLiveData<Int>{
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
        jsonObj_.put("query", "mutation{ registerUser(register: { firstName: \"$firstName\" lastName: \"$lastName\" username: \"$username\" password: \"$password\" correo: \"$correo\"}){ id } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.registerUser(gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){
                    liveData.value = -1
                }
                else{
                    var data: JsonObject = response?.body()?.get("data") as JsonObject
                    if(data.get("registerUser").toString() == "null"){
                        liveData.value = -2
                    }
                    else {
                        liveData.value = data.getAsJsonObject("registerUser").get("id").asInt
                    }
                }
            }
        })
        // 0  - Error en la app
        // -1 - Error en la Api Gateway
        // -2 - Datos incorrectos
        return liveData
    }


    fun authenticateUser(username: String, password: String): MutableLiveData<User>{
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<User> = MutableLiveData<User>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "mutation{ authenticateUser(auth: {username: \"$username\" password: \"$password\" dispositivo: \"AndroidDefault\" }){ id username firstName lastName token }}")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.registerUser(gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){
                    liveData.value = null
                }
                else{

                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {
                        liveData.value = null
                    }
                    else{
                        var data: JsonObject = dataAux as JsonObject

                        var authenticateUser: JsonObject = data?.get("authenticateUser") as JsonObject
                        var idAux: Int = authenticateUser.get("id").asInt
                        var usernameAux: String = authenticateUser.get("username").asString
                        var firstnameAux: String = authenticateUser.get("firstName").asString
                        var lastnameAux: String = authenticateUser.get("lastName").asString
                        var token: String = authenticateUser.get("token").asString

                        liveData.value = User(idAux, usernameAux, firstnameAux, lastnameAux, token)

                    }


                }
            }
        })
        return liveData
    }




}