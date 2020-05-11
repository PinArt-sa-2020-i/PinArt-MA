package com.example.pinart_ma.service.repository

import androidx.lifecycle.MutableLiveData
import com.example.pinart_ma.service.model.Board
import com.google.gson.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BoardRepository {

    var URL: String = "http://ec2-3-209-34-155.compute-1.amazonaws.com:5000"

    companion object {
        @Volatile private var boardRepository: BoardRepository? = null

        fun getInstance() =
            boardRepository?: synchronized(this) {
                boardRepository ?: BoardRepository().also { boardRepository  = it }
            }
    }

    fun createBoard(token: String?, idUser: String?, name: String?, descripcion: String?): MutableLiveData<Int> {

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
        jsonObj_.put("query", "mutation{ createBoard(board: { name: \"$name\" description: \"$descripcion\" user_id: $idUser}){ id } }")
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
                        var createBoard : JsonElement = data.get("createBoard") as JsonElement

                        if (createBoard is JsonNull){
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


    fun getBoardsUser(token: String?, idUser: String?): MutableLiveData<ArrayList<Board>> {

        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<ArrayList<Board>> = MutableLiveData<ArrayList<Board>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ allBoardsByUser(id: $idUser){ id name description  } }")
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
                        var allBoardsByUserAux : JsonElement = data.get("allBoardsByUser") as JsonElement

                        if (allBoardsByUserAux is JsonNull){
                            liveData.value = arrayListOf()
                        }
                        else{
                            var allBoardsByUser: JsonArray = data.get("allBoardsByUser") as JsonArray
                            var boards: ArrayList<Board> = arrayListOf()

                            for(i in 0 until allBoardsByUser.size()){
                                var boardAux: JsonObject = allBoardsByUser[i] as JsonObject
                                var board : Board = Board(
                                    boardAux.get("id").asString,
                                    boardAux.get("name").asString,
                                    boardAux.get("description").asString,
                                    idUser
                                )
                                boards.add(board)
                            }
                            liveData.value = boards
                        }
                    }
                }
            }
        })
        return liveData
    }


    fun getBoardsFollowByUser(token: String?, idUser: String?): MutableLiveData<ArrayList<Board>> {
        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<ArrayList<Board>> = MutableLiveData<ArrayList<Board>>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query { getAllBoardsFollowByUser(user_id: $idUser)  { id  name description } }")
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
                        var  getAllBoardsFollowByUserAux : JsonElement = data.get("getAllBoardsFollowByUser") as JsonElement

                        if (getAllBoardsFollowByUserAux is JsonNull){
                            liveData.value = arrayListOf()
                        }
                        else{
                            var getAllBoardsFollowByUser: JsonArray = data.get("getAllBoardsFollowByUser") as JsonArray
                            var boards: ArrayList<Board> = arrayListOf()

                            for(i in 0 until getAllBoardsFollowByUser.size()){
                                var boardAux: JsonObject = getAllBoardsFollowByUser[i] as JsonObject
                                var board : Board = Board(
                                    boardAux.get("id").asString,
                                    boardAux.get("name").asString,
                                    boardAux.get("description").asString,
                                    null
                                )
                                boards.add(board)
                            }
                            liveData.value = boards
                        }
                    }
                }
            }
        })
        return liveData
    }


    fun addMultimediaBoard(token: String?, idMultimedia: String?, idBoard: String?): MutableLiveData<Int> {

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
        jsonObj_.put("query", "mutation{ addMultimediaTablero(idMultimedia: \"$idMultimedia\", idTablero: \"$idBoard\"){id}}")
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
                        var addMultimediaTablero : JsonElement = data.get("addMultimediaTablero") as JsonElement

                        if (addMultimediaTablero is JsonNull){
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

    fun removeMultimediaBoard(token: String?, idMultimedia: String?, idBoard: String?): MutableLiveData<Int> {

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
        jsonObj_.put("query", "mutation{ deleteMultimediaTablero(idMultimedia: \"$idMultimedia\", idTablero: \"$idBoard\"){id}}")
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
                        var addMultimediaTablero : JsonElement = data.get("deleteMultimediaTablero") as JsonElement

                        if (addMultimediaTablero is JsonNull){
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