package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.adapter.BoardsFeedAdapter
import com.example.pinart_ma.ui.adapter.ProfileMultimediaAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.fragment_list_boards.*
import kotlinx.android.synthetic.main.profile_multimedia_fragment.*

class ListBoardsFragment(var idUsuario : String?, var feed: Boolean) : Fragment() {
    companion object {
        fun newInstance(idUsuario: String?, mine: Boolean): ListBoardsFragment = ListBoardsFragment(idUsuario, mine)
    }

    var boards : ArrayList<Board> = arrayListOf()
    var multimedia: ArrayList<ArrayList<Multimedia>> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_list_boards, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoards(context)
    }

    fun loadBoards(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        //Guardar Datos
        var boardFactory =InjectorUtils.providerBoardViewModelFactory()
        var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)


        if(feed == false){
            boardViewModel!!.getBoardsUser(token, idUsuario).observe(viewLifecycleOwner, Observer {
                boardsResponse ->
                for(boardAux in boardsResponse){
                    boards.add(boardAux)
                }
                conatinerRecyclerViewListBoards.layoutManager = LinearLayoutManager(context)
                conatinerRecyclerViewListBoards.adapter = BoardsFeedAdapter(boards, this)
            })
        }
        else{
            boardViewModel!!.getBoardsFollowByUser(token, idUsuario).observe(viewLifecycleOwner, Observer {
                    boardsResponse ->
                for(boardAux in boardsResponse){
                    boards.add(boardAux)
                }
                conatinerRecyclerViewListBoards.layoutManager = LinearLayoutManager(context)
                conatinerRecyclerViewListBoards.adapter = BoardsFeedAdapter(boards, this)
            })
        }


    }
}