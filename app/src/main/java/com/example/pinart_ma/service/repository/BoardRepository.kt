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

    var URL: String = "http://ec2-52-3-175-38.compute-1.amazonaws.com:5000"

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

    fun getBoardById(token: String?, idBoard: String?): MutableLiveData<Board> {

        //Se crea el retrofic api
        var api: APIGateway
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIGateway::class.java)

        //Live data a retornar
        val liveData: MutableLiveData<Board> = MutableLiveData<Board>()

        //Mapeo los datos
        val jsonObj_ = JSONObject()
        jsonObj_.put("query", "query{ boardById(id: $idBoard){ id name description user{ id } } }")
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
                        var boardAux : JsonElement = data.get("boardById") as JsonElement

                        if (boardAux is JsonNull){
                            liveData.value = null
                        }
                        else{
                            var boardJson : JsonObject = data.get("boardById") as JsonObject
                            var userJson : JsonObject = boardJson.get("user") as JsonObject

                            liveData.value = Board(
                                boardJson.get("id").asString,
                                boardJson.get("name").asString,
                                boardJson.get("description").asString,
                                userJson.get("id").asString
                            )
                        }
                    }
                }
            }
        })
        return liveData
    }


    fun getAllBoardFollow(token: String?): MutableLiveData<ArrayList<ArrayList<String>>>{
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
        jsonObj_.put("query", "query{ allBoardFollow{ id user{ id } board{ id } } }")
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

                        var listfollowsJson: JsonArray = data.getAsJsonArray("allBoardFollow")
                        var listFollows: ArrayList<ArrayList<String>> = arrayListOf()

                        for (i in 0 until listfollowsJson.size()){
                            var followJson : JsonObject = listfollowsJson[i] as JsonObject
                            var userJson: JsonObject = followJson.get("user") as JsonObject
                            var boardJson: JsonObject = followJson.get("board") as JsonObject

                            var follow : ArrayList<String> = arrayListOf(
                                followJson.get("id").asString,
                                userJson.get("id").asString,
                                boardJson.get("id").asString
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


    fun deleteUserFollowBoard(token: String?, idUserFollowBoard: String?): MutableLiveData<Int> {

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
        jsonObj_.put("query", "mutation{ deleteBoardFollow(id: $idUserFollowBoard)}")
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
                        liveData.value = 1
                    }
                }
            }
        })
        return liveData
    }


    fun addUserFollowBoard(token: String?, idUser: String?, idBoard: String?): MutableLiveData<Int> {

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
        jsonObj_.put("query", "mutation{createBoardFollow(boardfollow: {board_id: $idBoard user_id: $idUser}){id }}")
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
                        var data: JsonObject = response?.body()?.get("data") as JsonObject
                        var createBoardFollowAux: JsonElement = data.get("createBoardFollow") as JsonElement

                        if (createBoardFollowAux is JsonNull) {
                            liveData.value = 0
                        }
                        else{
                            var createBoardFollow: JsonObject = data.get("createBoardFollow") as JsonObject
                            liveData.value = createBoardFollow.get("id").asInt
                        }

                    }
                }
            }
        })
        return liveData
    }
}