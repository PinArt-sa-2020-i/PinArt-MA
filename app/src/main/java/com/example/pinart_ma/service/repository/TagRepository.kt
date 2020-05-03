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

    var URL: String = "http://ec2-54-92-134-33.compute-1.amazonaws.com:5000"

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

}