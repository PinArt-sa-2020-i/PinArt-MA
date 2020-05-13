package com.example.pinart_ma.ui.adapter

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board
import com.example.pinart_ma.ui.MainActivity
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_board.view.*

class BoardsFeedAdapter(var boards : ArrayList<Board>, var fragment: Fragment) : RecyclerView.Adapter<BoardsFeedAdapter.BoardViewHolder>(){

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_board, parent, false)
        context = parent.context
        return BoardViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return  boards.size
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        var itemBoard = boards[position]
        holder.bingBoard(itemBoard, fragment, context)
    }


    class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bingBoard(board: Board, fragment: Fragment, context: Context){

            val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val token = myPreferences.getString("token", "unknown")
            val id = myPreferences.getString("id", "unknown")

            var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
            var multimediaViewModel = ViewModelProviders.of(fragment, multimediaFactory).get(
                MultimediaViewModel::class.java)

            multimediaViewModel!!.getMultimediaByBoard(token, board.id).observeForever(Observer {
                multimediaResult ->
                /*
                var urlImage01 = "https://addons-media.operacdn.com/media/CACHE/images/themes/75/123475/1.2-rev2/images/c186c706-84c7-40cd-9d8d-248eda9ba5da/75534491350f53839d850757ef164df3.jpg"
                var urlImage02 = "https://images.unsplash.com/photo-1518531933037-91b2f5f229cc?ixlib=rb-1.2.1&w=1000&q=80"
                var urlImage03 = "https://i.pinimg.com/736x/34/48/a3/3448a3fa4ed35e90e2ca5fd4831c8be9.jpg"
                */

                if(multimediaResult.size >= 1){
                    Picasso.get().load(multimediaResult[0].url).fit().centerCrop().into(itemView.imageView01ListItemBoard);
                }
                if(multimediaResult.size >= 2){
                    Picasso.get().load(multimediaResult[1].url).fit().centerCrop().into(itemView.imageView02ListItemBoard);
                }
                if(multimediaResult.size >=3){
                    Picasso.get().load(multimediaResult[2].url).fit().centerCrop().into(itemView.imageView03ListItemBoard);
                }
            })

            itemView.nameListItemBoard.text = board.name
            itemView.boardContainerFeed.clipToOutline = true;

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "boardFragment")
                intent.putExtra("idBoard", board.id)
                itemView.context.startActivity(intent)
            }

        }
    }
}





