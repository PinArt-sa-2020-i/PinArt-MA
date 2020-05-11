package com.example.pinart_ma.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import kotlinx.android.synthetic.main.list_item_board_select.view.*

class SaveMultimediaInBoardAdapter(
    var boards: ArrayList<Board>,
    var boardsSelect: ArrayList<Boolean>,
    var activity: FragmentActivity,
    var idImagen: String
) : RecyclerView.Adapter<SaveMultimediaInBoardAdapter.BoardViewHolder>(){

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_board_select, parent, false)
        context = parent.context
        return BoardViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        var itemBoard = boards[position]
        var select = boardsSelect[position]
        holder.bindBoard(itemBoard, select ,activity, context, idImagen)
    }

    class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindBoard(board: Board, select: Boolean, activity: FragmentActivity, context: Context, idMultimedia: String){

            var selectAux = select
            //Pintar datos
            itemView.nameItemBoardSelect.text = board.name
            if(select==true){
                itemView.nameItemBoardSelect.setBackgroundColor(Color.parseColor("#00FF00"))
            }

            //Funcionalidad
            itemView.setOnClickListener {
                val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                val id: String? = myPreferences.getString("id", "unknown")
                var token: String? = myPreferences.getString("token", "unknown")

                var boardFactory = InjectorUtils.providerBoardViewModelFactory()
                var boardViewModel = ViewModelProviders.of(activity, boardFactory).get(BoardViewModel::class.java)

                if(selectAux == false){
                    boardViewModel!!.addMultimediaBoard(token, idMultimedia, board.id).observe(activity, Observer {
                            result ->
                            if(result >0){
                                itemView.nameItemBoardSelect.setBackgroundColor(Color.parseColor("#00FF00"))
                                selectAux = true
                            }
                    })
                }
                else{
                    boardViewModel!!.removeMultimediaBoard(token, idMultimedia, board.id).observe(activity, Observer {
                            result ->
                            if(result >0){
                                itemView.nameItemBoardSelect.setBackgroundColor(Color.parseColor("#FFFFFF"))
                                selectAux = false
                            }
                    })
                }


            }
        }
    }

}