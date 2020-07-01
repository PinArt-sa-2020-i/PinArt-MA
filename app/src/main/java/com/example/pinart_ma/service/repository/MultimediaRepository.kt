package com.example.pinart_ma.service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Multimedia
import com.google.gson.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class MultimediaRepository {

    var URL: String = "http://ec2-52-3-175-38.compute-1.amazonaws.com:5000"

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
                liveData.value = arrayListOf()
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == "null"){liveData.value = arrayListOf()}
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
                                                            multimediaJsonAux.get("descripcion").asString,
                                                            multimediaJsonAux.get("usuario_creador_id").asString,null, null)


                            multimediaList.add(multimediaAux)
                        }
                        liveData.value = multimediaList
                    }
                }
            }
        })
        return liveData
    }

    fun getMultimediaByBoard(token: String?, idBoard: String?): MutableLiveData<MutableList<Multimedia>> {
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
        jsonObj_.put("query", "query{ getMultimediaByTable(id: \"$idBoard\"){ id descripcion url usuario_creador_id etiquetas_relacionadas_ids } }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.default(token, gsonObject)

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
                        var multimediaJsonAux: JsonElement = data.get("getMultimediaByTable") as JsonElement

                        if(multimediaJsonAux is JsonNull){
                            liveData.value = arrayListOf()
                        }
                        else{
                            var multimediaJsonList: JsonArray = data.getAsJsonArray("getMultimediaByTable")
                            var multimediaList: MutableList<Multimedia> = mutableListOf<Multimedia>()

                            for(i in 0 until multimediaJsonList.size()){
                                var multimediaJsonAux: JsonObject = multimediaJsonList[i] as JsonObject

                                var multimediaAux: Multimedia = Multimedia(
                                    multimediaJsonAux.get("id").asString,
                                    multimediaJsonAux.get("url").asString,
                                    multimediaJsonAux.get("descripcion").asString,
                                    multimediaJsonAux.get("usuario_creador_id").asString,null, null)


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
        jsonObj_.put("query", "query{ getUsersFeed(idUsuario: \"$idUser\"){ id descripcion url usuario_creador_id} }")
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
                                multimediaJsonAux.get("descripcion").asString,
                                multimediaJsonAux.get("usuario_creador_id").asString,null, null)

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
        jsonObj_.put("query", "query{ getTagsFeed(idUsuario: \"$idUser\"){id descripcion url usuario_creador_id} }")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.getMultimediaById(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = arrayListOf()
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body() == null){liveData.value = arrayListOf()}
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
                                    multimediaJsonAux.get("descripcion").asString,
                                    multimediaJsonAux.get("usuario_creador_id").asString,null, null)

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


    fun addMultimedia(idUser: String?, descripcion: String?, idEtiquetas: ArrayList<String?>,
                      url_imagen: String?, formato: String?, tamano: String?, idBucket: String?) : MutableLiveData<Int>{
        var api: APIMultimedia
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-52-3-175-38.compute-1.amazonaws.com:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIMultimedia::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("idUsuario", idUser)
        jsonObj_.put("descripcion", descripcion)
        jsonObj_.put("url_imagen", url_imagen)
        jsonObj_.put("formato", formato)
        jsonObj_.put("tamano", tamano)
        jsonObj_.put("id_bucket", idBucket)

        var idEtiquetasArr: JSONArray = JSONArray()

        for (i in 0 until idEtiquetas.size){
            idEtiquetasArr.put(idEtiquetas[i])
        }

        jsonObj_.put("idEtiquetas", idEtiquetasArr)

        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject
        Log.d("TAG", gsonObject.toString())
        var callApi = api.addMultimedia(gsonObject)

        callApi.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if(response?.code() == 200){
                    liveData.value = 1
                }
                else{
                    Log.d("Tag", response?.toString())
                    liveData.value = 0
                }
            }
        })

        return liveData
    }

    fun upLoadFileBucket(file: File?): MutableLiveData<String>{

        var api: BucketMS
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-52-3-175-38.compute-1.amazonaws.com:8085")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(BucketMS::class.java)

        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val call: Call<JsonObject> = api.addImagen(MultipartBody.Part.createFormData("file", ".jpg", requestBody))

        var idBucket :MutableLiveData<String> = MutableLiveData<String>()


        call.enqueue(object : Callback<JsonObject?> {
            override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                idBucket.value = null
            }

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                idBucket.value = response?.body()?.get("message")?.asString
            }

        })
        return idBucket
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
                                    multimediaJsonAux.get("descripcion").asString,
                                    multimediaJsonAux.get("usuario_creador_id").asString,null, null)

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


    fun getMultimediaById(token: String?, idMultimedia: String?): MutableLiveData<Multimedia> {
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Multimedia> = MutableLiveData<Multimedia>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ getMultimediaById(id: \"$idMultimedia\"){ id descripcion url  id_bucket usuario_creador_id etiquetas_relacionadas_ids tableros_agregados_ids} }")
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
                        var getMultimediaByIdAux : JsonElement = data.get("getMultimediaById") as JsonElement
                        if(getMultimediaByIdAux is JsonNull){
                            liveData.value = null
                        }
                        else{
                            var getMultimediaById : JsonObject = getMultimediaByIdAux as JsonObject

                            var etiquetasAsociadas: JsonArray = getMultimediaById.getAsJsonArray("etiquetas_relacionadas_ids")
                            var etiquetas: ArrayList<String> = arrayListOf()
                            for(i in 0 until  etiquetasAsociadas.size()){
                                etiquetas.add(etiquetasAsociadas[i].asString)
                            }

                            var tablerosAsociados: JsonArray = getMultimediaById.getAsJsonArray("tableros_agregados_ids")
                            var tableros: ArrayList<String> = arrayListOf()
                            for(i in 0 until  tablerosAsociados.size()){
                                tableros.add(tablerosAsociados[i].asString)
                            }

                            var multimedia: Multimedia = Multimedia(
                                getMultimediaById.get("id").asString,
                                getMultimediaById.get("url").asString,
                                getMultimediaById.get("descripcion").asString,
                                getMultimediaById.get("usuario_creador_id").asString,
                                etiquetas,
                                tableros
                                )
                            liveData.value = multimedia
                        }
                    }
                }
            }
        })
        return liveData
    }

    fun deleteMultimediaById(token: String?, idMultimedia: String?): MutableLiveData<Int> {
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
        jsonObj_.put("query", "mutation{ deleteMultimedia(idMultimedia: \"$idMultimedia\")}")
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject

        var callApi = api.getMultimediaById(token, gsonObject)

        //Se realiza la llamada
        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = null
            }
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body().toString() == null){liveData.value = 0}
                else{
                    var dataAux: JsonElement = response?.body()?.get("data") as JsonElement

                    if (dataAux is JsonNull) {liveData.value = 0}
                    else{
                        var data: JsonObject = dataAux as JsonObject
                        var deleteMultimediaAux : JsonElement = data.get("deleteMultimedia") as JsonElement
                        if(deleteMultimediaAux is JsonNull){
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