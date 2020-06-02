package com.example.pinart_ma.service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FCMRepository {



    companion object {
        @Volatile private var fcmRepository: FCMRepository? = null

        fun getInstance() =
            fcmRepository?: synchronized(this) {
                fcmRepository ?: FCMRepository().also { fcmRepository  = it }
            }
    }

    fun suscriberTopic(idUser: String?, token:String?) : MutableLiveData<Int> {
        Log.d("TAG", token)
        val baseUrl: String = "https://iid.googleapis.com/iid/v1/$token/rel/topics/$idUser/";
        var api: FCM
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://iid.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FCM::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.defaultRequest(
            baseUrl,
            "application/json",
            "key=AAAAixtvI3A:APA91bHdc3TFyBCylBcZ9NQbheFj2uzxKBolaO9kzwI0WhD4eKEf2Jv4B7QamUSHovqk6lmadqBf-A2FijAmzMO8G_YOYVvr_yjadvz8Yt3avop3dzNAwOOyPZxsEVzhDMo8YzhP5OVl",
            "0",
            gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if(response?.code() == 200){
                    liveData.value = 1
                }
                else{
                    Log.d("Tag", response?.code().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.headers().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.raw().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", call?.request().toString())
                    liveData.value = 0
                }
            }
        })

        return liveData
    }

    fun unsuscriberTopic(idUser: String?, token:String?) : MutableLiveData<Int> {

        val baseUrl: String = "https://iid.googleapis.com/iid/v1/$token/rel/topics/$idUser/";

        var api: FCM
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://iid.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FCM::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject


        var callApi = api.unsuscriberTopic(
            baseUrl,
            "application/json",
            "key=AAAAixtvI3A:APA91bHdc3TFyBCylBcZ9NQbheFj2uzxKBolaO9kzwI0WhD4eKEf2Jv4B7QamUSHovqk6lmadqBf-A2FijAmzMO8G_YOYVvr_yjadvz8Yt3avop3dzNAwOOyPZxsEVzhDMo8YzhP5OVl"
            )

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if(response?.code() == 200){
                    liveData.value = 1
                }
                else{
                    Log.d("Tag", response?.code().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.headers().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.raw().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", call?.request().toString())
                    liveData.value = 0
                }
            }
        })

        return liveData
    }



    fun notifyTopic(idUser: String?, tittle:String?, message: String?) : MutableLiveData<Int> {
        val baseUrl: String = "https://fcm.googleapis.com/fcm/send";

        var api: FCM
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://iid.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FCM::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()



        jsonObj_.put("to", "/topics/$idUser")
        jsonObj_.put("priority", "high")

        val jsonObj_notification = JSONObject()
        jsonObj_notification.put("title", tittle)
        jsonObj_notification.put("body", message)

        jsonObj_.put("notification", jsonObj_notification)


        var gsonObject = JsonParser().parse(jsonObj_.toString()) as JsonObject
        var callApi = api.defaultRequest2(
            baseUrl,
            "application/json",
            "key=AAAAixtvI3A:APA91bHdc3TFyBCylBcZ9NQbheFj2uzxKBolaO9kzwI0WhD4eKEf2Jv4B7QamUSHovqk6lmadqBf-A2FijAmzMO8G_YOYVvr_yjadvz8Yt3avop3dzNAwOOyPZxsEVzhDMo8YzhP5OVl",
            gsonObject)

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = 0
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if(response?.code() == 200){
                    liveData.value = 1
                }
                else{
                    Log.d("Tag", response?.code().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.headers().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.raw().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", call?.request().toString())
                    liveData.value = 0
                }
            }
        })

        return liveData
    }


    fun getTopicsSuscriber(token:String?) : MutableLiveData<ArrayList<String>> {

        val baseUrl: String = "https://iid.googleapis.com/iid/info/$token?details=true";

        var api: FCM
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://iid.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FCM::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()


        var callApi = api.getTopicsSuscriber(
            baseUrl,
            "application/json",
            "key=AAAAixtvI3A:APA91bHdc3TFyBCylBcZ9NQbheFj2uzxKBolaO9kzwI0WhD4eKEf2Jv4B7QamUSHovqk6lmadqBf-A2FijAmzMO8G_YOYVvr_yjadvz8Yt3avop3dzNAwOOyPZxsEVzhDMo8YzhP5OVl"
        )

        callApi.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = arrayListOf()
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if(response?.code() == 200){
                    try{
                        val response: JSONObject = JSONObject(
                            response.body().getAsJsonObject("rel").getAsJsonObject("topics")
                                .toString()
                        );
                        val keys = response.keys();
                        val topics: ArrayList<String> = arrayListOf();

                        while (keys.hasNext()) {
                            val key = keys.next();
                            topics.add(key)
                        }
                        Log.d("TAG", topics.toString())
                        liveData.value = topics;
                    }catch(e: Exception){
                        liveData.value = arrayListOf()
                        Log.d("TAG", "vacio")
                    }

                }


                else{
                    Log.d("Tag", response?.code().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.headers().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", response?.raw().toString())
                    Log.d("Tag", ".")
                    Log.d("Tag", call?.request().toString())
                    liveData.value = arrayListOf()
                }
            }
        })

        return liveData
    }
}