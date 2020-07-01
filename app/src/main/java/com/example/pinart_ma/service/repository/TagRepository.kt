package com.example.pinart_ma.service.repository

import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.service.model.User
import com.google.gson.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TagRepository {

    var URL: String = "http://ec2-52-3-175-38.compute-1.amazonaws.com:5000"

    companion object {
        @Volatile private var tagRepository: TagRepository? = null

        fun getInstance() =
            tagRepository?: synchronized(this) {
                tagRepository ?: TagRepository().also { tagRepository  = it }
            }
    }


    fun getAllTags(token: String?): MutableLiveData<MutableList<Tag>> {

        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<MutableList<Tag>> = MutableLiveData<MutableList<Tag>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getAllLabels{ id name description } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject



        //Se realiza la llamada
        var callApi = api.getAllUser(token, gsonObject)

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
                        var tagJsonListAux: JsonElement = data.get("getAllLabels") as JsonElement

                        if (tagJsonListAux is JsonNull){
                            liveData.value = arrayListOf()
                        }
                        else{
                            var tagJsonList: JsonArray = data.getAsJsonArray("getAllLabels")
                            var tagList: MutableList<Tag> = mutableListOf<Tag>()

                            for(i in 0 until tagJsonList.size()){
                                var tagJsonAux: JsonObject = tagJsonList[i] as JsonObject
                                var tagAux = Tag(
                                    tagJsonAux.get("id").asString,
                                    tagJsonAux.get("name").asString,
                                    tagJsonAux.get("description").asString)

                                tagList.add(tagAux)
                            }
                            liveData.value = tagList
                        }
                    }
                }
            }
        })
        return liveData
    }


    fun getTagById(token: String?, idTag: String?): MutableLiveData<Tag> {

        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Tag> = MutableLiveData<Tag>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ labelById(id: $idTag){ id name description } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject



        //Se realiza la llamada
        var callApi = api.default(token, gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null) {
                    liveData.value = null
                } else {
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {
                        liveData.value = null
                    } else {
                        var data: JsonObject = dataAux as JsonObject
                        var tagJsonListAux: JsonElement = data.get("labelById") as JsonElement

                        if (tagJsonListAux is JsonNull) {
                            liveData.value = null
                        } else {
                            var tagJson: JsonObject = data.get("labelById") as JsonObject

                            var tagAux = Tag(
                                tagJson.get("id").asString,
                                tagJson.get("name").asString,
                                tagJson.get("description").asString
                            )
                            liveData.value = tagAux
                        }

                    }
                }
            }
        })
        return liveData
    }

    fun getTagFolledByUser(token: String?, idUser: String?): MutableLiveData<ArrayList<String>> {

        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ userLabels(userId: $idUser){ relatedLabels{ id } } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject



        //Se realiza la llamada
        var callApi = api.default(token, gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = arrayListOf()
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null) {
                    liveData.value = arrayListOf()
                } else {
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {
                        liveData.value = arrayListOf()
                    } else {
                        var data: JsonObject = dataAux as JsonObject
                        var userLabelAux : JsonElement = data.get("userLabels") as JsonElement

                        if (userLabelAux is JsonNull){
                            liveData.value = arrayListOf()
                        }
                        else{
                            var userLabel : JsonObject = data.get("userLabels") as JsonObject
                            var relatedLabelsAux : JsonElement = userLabel.get("relatedLabels") as JsonElement

                            if(relatedLabelsAux is JsonNull){
                                liveData.value = arrayListOf()
                            }
                            else{
                                var relatedLabels: JsonArray = userLabel.get("relatedLabels") as JsonArray
                                var tags: ArrayList<String> = arrayListOf()
                                for(i in 0 until  relatedLabels.size()){
                                    var tagAux: JsonObject = relatedLabels[i] as JsonObject
                                    tags.add(tagAux.get("id").asString)
                                }
                                liveData.value = tags
                            }
                        }
                    }
                }
            }
        })
        return liveData
    }

    fun followTag(token: String?, idUser: String?, idTag: String?): MutableLiveData<Int> {

        //Se crea el retrofic api
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
        jsonObj_.put("query", "mutation{ addLabelUser(idUser: $idUser, idLabel: $idTag){ id } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject



        //Se realiza la llamada
        var callApi = api.default(token, gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null) {
                    liveData.value = 0
                } else {
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {
                        liveData.value = 0
                    } else {
                        var data: JsonObject = dataAux as JsonObject
                        var addLabelUserAux : JsonElement = data.get("addLabelUser") as JsonElement

                        if (addLabelUserAux is JsonNull){
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


    fun unFollowTag(token: String?, idUser: String?, idTag: String?): MutableLiveData<Int> {

        //Se crea el retrofic api
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
        jsonObj_.put("query", "mutation{ removeLabelUser(idUser: $idUser, idLabel:$idTag) }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject



        //Se realiza la llamada
        var callApi = api.default(token, gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null) {
                    liveData.value = 0
                } else {
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {
                        liveData.value = 0
                    } else {
                        var data: JsonObject = dataAux as JsonObject
                        var removeLabelUserAux : JsonElement = data.get("removeLabelUser") as JsonElement

                        if (removeLabelUserAux is JsonNull){
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