package com.example.pinart_ma.service.repository

import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.User
import com.google.gson.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MultimediaRepository {

    var URL: String = "http://ec2-54-92-134-33.compute-1.amazonaws.com:5000"

    companion object {
        @Volatile private var multimediaRepository: MultimediaRepository? = null

        fun getInstance() =
            multimediaRepository?: synchronized(this) {
                multimediaRepository ?: MultimediaRepository().also { multimediaRepository  = it }
            }
    }

    fun getMultimediaByUser(token: String?, idUser: String?): MutableLiveData<MutableList<Multimedia>> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<Multimedia>> = MutableLiveData<MutableList<Multimedia>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getMultimediaByUser(id: \"$idUser\"){ id descripcion url id_bucket usuario_creador_id etiquetas_relacionadas_ids tableros_agregados_ids }}")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.getMultimediaById(token, gsonObject)

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
                        var multimediaJsonList: JsonArray = data.getAsJsonArray("getMultimediaByUser")
                        var multimediaList: MutableList<Multimedia> = mutableListOf<Multimedia>()

                        for(i in 0 until multimediaJsonList.size()){
                            var multimediaJsonAux: JsonObject = multimediaJsonList[i] as JsonObject
                            var multimediaAux: Multimedia = Multimedia(
                                                            multimediaJsonAux.get("id").asString,
                                                            multimediaJsonAux.get("url").asString,
                                                            multimediaJsonAux.get("descripcion").asString)

                            multimediaList.add(multimediaAux)
                        }
                        liveData.value = multimediaList
                    }
                }
            }
        })
        return liveData
    }

    fun getFeedUsers(token: String?, idUser: String?): MutableLiveData<MutableList<Multimedia>> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<Multimedia>> = MutableLiveData<MutableList<Multimedia>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getUsersFeed(idUsuario: \"$idUser\"){ id descripcion url } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.getMultimediaById(token, gsonObject)

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
                        var multimediaJsonList: JsonArray = data.getAsJsonArray("getUsersFeed")
                        var multimediaList: MutableList<Multimedia> = mutableListOf<Multimedia>()

                        for(i in 0 until multimediaJsonList.size()){
                            var multimediaJsonAux: JsonObject = multimediaJsonList[i] as JsonObject
                            var multimediaAux = Multimedia(
                                multimediaJsonAux.get("id").asString,
                                multimediaJsonAux.get("url").asString,
                                multimediaJsonAux.get("descripcion").asString)

                            multimediaList.add(multimediaAux)
                        }
                        liveData.value = multimediaList
                    }
                }
            }
        })
        return liveData
    }

    fun getFeedTags(token: String?, idUser: String?): MutableLiveData<MutableList<Multimedia>> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<Multimedia>> = MutableLiveData<MutableList<Multimedia>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getTagsFeed(idUsuario: \"$idUser\"){id descripcion url } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.getMultimediaById(token, gsonObject)

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

                        var multimediaJsonListAux: JsonElement = data.get("getTagsFeed") as JsonElement
                        if(multimediaJsonListAux is JsonNull){liveData.value = arrayListOf()}
                        else{
                            var multimediaJsonList: JsonArray = data.getAsJsonArray("getTagsFeed")
                            var multimediaList: MutableList<Multimedia> = mutableListOf<Multimedia>()

                            for(i in 0 until multimediaJsonList.size()){
                                var multimediaJsonAux: JsonObject = multimediaJsonList[i] as JsonObject
                                var multimediaAux = Multimedia(
                                    multimediaJsonAux.get("id").asString,
                                    multimediaJsonAux.get("url").asString,
                                    multimediaJsonAux.get("descripcion").asString)

                                multimediaList.add(multimediaAux)
                            }
                            liveData.value = multimediaList
                        }

                    }
                }
            }
        })
        return liveData
    }

    fun getMultimediaByTag(token: String?, idTag: String?): MutableLiveData<MutableList<Multimedia>> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<Multimedia>> = MutableLiveData<MutableList<Multimedia>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getMultimediaByTag(id: \"$idTag\"){ id descripcion url usuario_creador_id } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.getMultimediaById(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = arrayListOf()
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = arrayListOf()}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {liveData.value = arrayListOf()}
                    else{
                        var data: JsonObject = dataAux as JsonObject
                        var multimediaJsonListAux: JsonElement = data.get("getMultimediaByTag") as JsonElement

                        if(multimediaJsonListAux is JsonNull) {
                            liveData.value = arrayListOf()
                        }
                        else{
                            var multimediaJsonList: JsonArray = data.getAsJsonArray("getMultimediaByTag")
                            var multimediaList: MutableList<Multimedia> = mutableListOf<Multimedia>()

                            for(i in 0 until multimediaJsonList.size()){
                                var multimediaJsonAux: JsonObject = multimediaJsonList[i] as JsonObject
                                var multimediaAux: Multimedia = Multimedia(
                                    multimediaJsonAux.get("id").asString,
                                    multimediaJsonAux.get("url").asString,
                                    multimediaJsonAux.get("descripcion").asString)

                                multimediaList.add(multimediaAux)
                            }
                            liveData.value = multimediaList
                        }

                    }
                }
            }
        })
        return liveData
    }

}