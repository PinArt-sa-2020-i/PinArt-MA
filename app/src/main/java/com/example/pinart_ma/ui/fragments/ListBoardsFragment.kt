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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Board
import com.example.pinart_ma.ui.adapter.BoardsFeedAdapter
import com.example.pinart_ma.ui.adapter.ProfileMultimediaAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.fragment_list_boards.*
import kotlinx.android.synthetic.main.profile_multimedia_fragment.*

class ListBoardsFragment(var idUsuario : String?, var mine: Boolean) : Fragment() {
    companion object {
        fun newInstance(idUsuario: String?, mine: Boolean): ListBoardsFragment = ListBoardsFragment(idUsuario, mine)
    }

    var boards : ArrayList<Board> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_list_boards, container, false)

        return view
    }

    fun loadBoards(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        //Guardar Datos
        var boardFactory =InjectorUtils.providerBoardViewModelFactory()
        var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

        boardViewModel!!.getBoardsUser(token, idUsuario).observe(viewLifecycleOwner, Observer {
            boardsResponse ->
            for(boardAux in boardsResponse){
                boards.add(boardAux)
            }
            conatinerRecyclerViewListBoards.layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)
            conatinerRecyclerViewListBoards.adapter = BoardsFeedAdapter(boards)

        })



    }
}