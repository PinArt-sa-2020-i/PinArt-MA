package com.example.pinart_ma.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board

class BoardsFeedAdapter(var boards : ArrayList<Board>) : RecyclerView.Adapter<BoardsFeedAdapter.BoardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_board, parent, false)
        return BoardViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return  boards.size
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        var itemBoard = boards[position]
        holder.bingBoard(itemBoard)
    }


    class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bingBoard(board: Board){


        }
    }
}