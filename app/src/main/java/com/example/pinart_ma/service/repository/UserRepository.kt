package com.example.pinart_ma.service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.User
import com.google.gson.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserRepository {

    var URL: String = "http://ec2-52-3-175-38.compute-1.amazonaws.com:5000"

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
        jsonObj_.put("query", "mutation{ authenticateUser(auth: { username: \"$username\" password: \"$password\" dispositivo: \"Mobile\" }){ id idSesion username firstName lastName token } }")
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
                    Log.d("TAG", response?.body().toString())
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
                        var idSesion: Int = authenticateUser.get("idSesion").asInt
                        liveData.value = User(idAux, usernameAux, firstnameAux, lastnameAux, null, token, null, null, null, null, null, idSesion)
                    }
                }
            }
        })
        return liveData
    }

    fun getAllUsers(token: String?): MutableLiveData<MutableList<User>> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<User>> = MutableLiveData<MutableList<User>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ allUsers{ id firstName lastName username correo profiles{ foto }}}")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.getAllUser(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = arrayListOf()}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {liveData.value = arrayListOf()}
                    else{
                        var data: JsonObject = dataAux as JsonObject

                        var userJsonList: JsonArray = data.getAsJsonArray("allUsers")
                        var userList: MutableList<User> = mutableListOf<User>()

                        for(i in 0 until userJsonList.size()){
                            var userJsonAux: JsonObject = userJsonList[i] as JsonObject

                            var profiles: JsonArray =userJsonAux.get("profiles") as JsonArray
                            var profile: JsonObject = profiles[0] as JsonObject


                            var fotoAux: JsonElement = profile.get("foto") as JsonElement
                            var foto: String?
                            if(fotoAux is JsonNull){foto=null}
                            else{foto = profile.get("foto").asString}

                            var userAux = User(
                                userJsonAux.get("id").asInt,
                                userJsonAux.get("username").asString,
                                userJsonAux.get("firstName").asString,
                                userJsonAux.get("lastName").asString,
                                null,
                                null, null, foto, null, null, null, null)

                            userList.add(userAux)
                        }
                        liveData.value = userList
                    }
                }
            }
        })
        return liveData
    }

    fun getUserById(token: String?, userId: String?): MutableLiveData<User> {
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
        jsonObj_.put("query", "query{ userById(id: $userId){ id firstName lastName username correo profiles{ genero foto descripcion noTelefono edad }}}")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.getUserById(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = null}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {liveData.value = null}
                    else{
                        var data: JsonObject = dataAux as JsonObject

                        var userJson: JsonObject = data.getAsJsonObject("userById")

                        var profiles: JsonArray =userJson.get("profiles") as JsonArray
                        var profile: JsonObject = profiles[0] as JsonObject


                        var generoAux: JsonElement = profile.get("genero") as JsonElement
                        var genero: String?
                        if(generoAux is JsonNull){genero=null}
                        else{genero = profile.get("genero").asString}

                        var fotoAux: JsonElement = profile.get("foto") as JsonElement
                        var foto: String?
                        if(fotoAux is JsonNull){foto=null}
                        else{foto = profile.get("foto").asString}


                        var descripcionAux: JsonElement = profile.get("descripcion") as JsonElement
                        var descripcion: String?
                        if(descripcionAux is JsonNull){descripcion=null}
                        else{descripcion = profile.get("descripcion").asString}


                        var noTelefonoAux: JsonElement = profile.get("noTelefono") as JsonElement
                        var noTelefono: String?
                        if(noTelefonoAux is JsonNull){noTelefono=null}
                        else{noTelefono = profile.get("noTelefono").asString}

                        var edadAux: JsonElement = profile.get("edad") as JsonElement
                        var edad: String?
                        if(edadAux is JsonNull){edad=null}
                        else{edad = profile.get("edad").asString}

                        liveData.value = User(
                            userJson.get("id").asInt,
                            userJson.get("username").asString,
                            userJson.get("firstName").asString,
                            userJson.get("lastName").asString,
                            userJson.get("correo").asString,
                            null,
                            genero,
                            foto,
                            descripcion,
                            noTelefono,
                            edad,
                            null
                        )
                    }
                }
            }
        })
        return liveData
    }

    fun getAllUserFollow(token: String?): MutableLiveData<ArrayList<ArrayList<String>>>{
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData<ArrayList<ArrayList<String>>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ allUserFollow{ id userFollower{ id } userFollowing { id } } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.getUserById(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = arrayListOf()}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {liveData.value = arrayListOf()}
                    else{
                        var data: JsonObject = dataAux as JsonObject

                        var listfollowsJson: JsonArray = data.getAsJsonArray("allUserFollow")
                        var listFollows: ArrayList<ArrayList<String>> = arrayListOf()

                        for (i in 0 until listfollowsJson.size()){
                            var followJson : JsonObject = listfollowsJson[i] as JsonObject
                            var followerJson: JsonObject = followJson.get("userFollower") as JsonObject
                            var followingJson: JsonObject = followJson.get("userFollowing") as JsonObject

                            var follow : ArrayList<String> = arrayListOf(
                                followJson.get("id").asString,
                                followerJson.get("id").asString,
                                followingJson.get("id").asString
                            )
                            listFollows.add(follow)
                        }
                        liveData.value = listFollows


                    }
                }
            }
        })
        return liveData
    }


    fun deleteUserFollow(token: String?, idFollow: String?): MutableLiveData<Int>{
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
        jsonObj_.put("query", "mutation{deleteUserFollow(id: $idFollow)}")

        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.default(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = 0}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement
                    if(dataAux is JsonNull){liveData.value = 0}
                    else{liveData.value = 1}
                }
            }
        })
        return liveData
    }

    fun createUserFollow(token: String?, idFollower: String?, idFollowing: String?): MutableLiveData<Int>{
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
        jsonObj_.put("query", "mutation{ createUserFollow(userfollow:{ userFollower_id: $idFollower userFollowing_id: $idFollowing } ){ id}}")

        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.default(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = 0}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement
                    if(dataAux is JsonNull){
                        liveData.value = 0
                    }
                    else{
                        var data: JsonObject = response?.body()?.get("data") as JsonObject
                        var followAux: JsonElement = data.get("createUserFollow") as JsonElement
                        if(followAux is JsonNull){
                            liveData.value = 0
                        }
                        else{
                            var follow: JsonObject = data.get("createUserFollow") as JsonObject
                            liveData.value = follow.get("id").asInt
                        }
                    }
                }
            }
        })
        return liveData
    }


    fun deleteSesion(token: String?, idSesion: String?): MutableLiveData<Int>{
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
        jsonObj_.put("query", "mutation{ deleteSession(id: $idSesion) }")

        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.default(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = 0}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement
                    if(dataAux is JsonNull){
                        liveData.value = 0
                    }
                    else{
                        liveData.value = 1
                    }
                }
            }
        })
        return liveData
    }



    fun updateUserProfile(token: String?,
                          idUser:String?,
                          foto: String?,
                          descripcion: String,
                          noTelefono: String,
                          edad: String,
                          genero: String): MutableLiveData<Int>{

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
        jsonObj_.put("query", "mutation{ updateProfile(id: 0, profile: { userId: $idUser countryId: 1 foto: \"$foto\" descripcion: \"$descripcion\" noTelefono: \"$noTelefono\" edad: \"$edad\" genero: \"$genero\" })}")

        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.default(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = 0}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement
                    if(dataAux is JsonNull){
                        liveData.value = 0
                    }
                    else{
                        var data: JsonObject = response?.body()?.get("data") as JsonObject
                        var followAux: JsonElement = data.get("updateProfile") as JsonElement
                        if(followAux is JsonNull){
                            liveData.value = 0
                        }
                        else{
                            liveData.value = 1
                        }
                    }
                }
            }
        })
        return liveData
    }



}