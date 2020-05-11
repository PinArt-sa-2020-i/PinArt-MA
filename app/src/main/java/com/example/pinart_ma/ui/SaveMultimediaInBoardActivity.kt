package com.example.pinart_ma.ui

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.adapter.SaveMultimediaInBoardAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.activity_save_multimedia_in_board.*

class SaveMultimediaInBoardActivity : AppCompatActivity(){

    lateinit var multimedia: Multimedia
    var boards: ArrayList<Board> = arrayListOf()
    var boardsSelect: ArrayList<Boolean> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_multimedia_in_board)
        //Trae la multimedia
        loadMultimedia(intent.getStringExtra("idMultimedia"), this)

    }


    fun loadMultimedia(idMultimedia: String, context: Context){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var token: String? = myPreferences.getString("token", "unknown")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java)

        multimediaViewModel!!.getMultimediaById(token, idMultimedia).observe(this,  Observer {
            multimediaResponse ->
            if(multimediaResponse == null){ finish()}
            else{
                multimedia = multimediaResponse

                //Se cargan los tableros del usuario
                loadBoards(context)
            }
        })
    }

    fun loadBoards(context: Context){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id: String? = myPreferences.getString("id", "unknown")
        var token: String? = myPreferences.getString("token", "unknown")

        //Guardar Datos
        var boardFactory =InjectorUtils.providerBoardViewModelFactory()
        var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

        boardViewModel!!.getBoardsUser(token, id).observe(this, Observer {
            boardsResponse ->
            for(boardAux in boardsResponse){
                boards.add(boardAux)
                if (multimedia.tablerosRelacionados!!.contains(boardAux.id)){boardsSelect.add(true)}
                else{boardsSelect.add(false) }
            }

            //Muestra los tableros
            showBoards(context)
        })
    }

    fun showBoards(context: Context){
        recyclerViewSaveMultimediaInBoard.layoutManager = LinearLayoutManager(context)
        recyclerViewSaveMultimediaInBoard.adapter = SaveMultimediaInBoardAdapter(boards, boardsSelect, this, multimedia.id)
    }
}