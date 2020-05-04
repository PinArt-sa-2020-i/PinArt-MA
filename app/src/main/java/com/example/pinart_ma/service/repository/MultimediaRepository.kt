package com.example.pinart_ma.service.repository

import android.graphics.Bitmap
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Multimedia
import com.google.gson.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File


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

    fun addMultimedia( file: File?) : MutableLiveData<String>{
        return MutableLiveData()
    }

    fun upLoadFileBucket(file: File?): MutableLiveData<String>{
        var api: BucketMS
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-227-65-124.compute-1.amazonaws.com:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(BucketMS::class.java)

        val byteArrayOutputStream = ByteArrayOutputStream()

        val call: Call<JsonObject> = api.addImagen2(MultipartBody.Part.createFormData("file", ".jpg", RequestBody.create(MediaType.parse("image/*"), file)))

        var idBucket :MutableLiveData<String> = MutableLiveData<String>()

        idBucket.value = "siiiiiiiiiiiiu"
        call.enqueue(object : Callback<JsonObject?> {
            override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                idBucket.value = t.toString()

            }

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                idBucket.value = response?.body()?.get("message").toString()
                Log.d("TAGARDIÑO", response?.code().toString())
            }

        })
        return idBucket
    }
}