package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.content.Intent
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
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.MainActivity
import com.example.pinart_ma.ui.adapter.ProfileMultimediaAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_view_image_activity.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.profile_multimedia_fragment.*

class BoardFragment(var idBoard: String?) : Fragment() {

    companion object {
        fun newInstance(idBoard: String?): BoardFragment = BoardFragment(idBoard)
    }

    lateinit var board: Board
    lateinit var ownerBoard: User
    var multimedia: ArrayList<Multimedia> = arrayListOf()
    var idFollowBoard: String = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_board, container, false)
        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoard()

        refreshBoard.setOnRefreshListener {
            multimedia = arrayListOf()
            loadBoard()
            refreshBoard.setRefreshing(false)
        }
    }



    fun loadBoard(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        //Guardar Datos
        var boardFactory = InjectorUtils.providerBoardViewModelFactory()
        var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

        boardViewModel!!.getBoardById(token, idBoard).observe(viewLifecycleOwner, Observer {
            boardResult ->
            if(boardResult == null){activity?.finish()}
            else{
                board = boardResult

                //Information Board
                nameBoard.text = board.name
                descriptionBoard.text = board.descripcion

                //Information User
                loadUser()

                //multimedia
                loadMultimediaBoard()


            }
        })
    }

    fun loadUser(){
        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")
        var id: String? = myPreferences.getString("id", "unknown")

        userViewModel!!.getUserById(token, board.idUsuario).observe(viewLifecycleOwner, Observer {
            userResult ->
            if(userResult == null){
                activity?.finish()
            }
            else{
                ownerBoard = userResult
                nameUserBoard.text = "Creador: ${ownerBoard.username}"


                if (id == board.idUsuario){
                    nameUserBoard.setOnClickListener {
                        var intent: Intent = Intent(activity,  MainActivity::class.java)
                        intent.putExtra("typeFragment", "myProfileFragment")
                        startActivity(intent)
                    }

                }else{
                    nameUserBoard.setOnClickListener {
                        var intent: Intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("typeFragment", "otherProfileFragment")
                        intent.putExtra("idUsuario", board.idUsuario)
                        startActivity(intent)
                    }
                }

                //LoadButtons
                loadFollowButton()
                loadButtonEdit()
                loadButtondelete()

            }
        })
    }


    fun loadMultimediaBoard(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java);

        multimediaViewModel!!.getMultimediaByBoard(token, idBoard).observe(viewLifecycleOwner, Observer {
            multimediaResult ->
            for(multimediaAux in multimediaResult){
                multimedia.add(multimediaAux)
            }
            recyclerViewBoard.layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)
            recyclerViewBoard.adapter = ProfileMultimediaAdapter(multimedia)

        })

    }



    fun loadFollowButton(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")
        if(id == board.idUsuario){
            followBoard.visibility = View.GONE
        }
        else{
            followBoard.visibility = View.VISIBLE
            val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val token = myPreferences.getString("token", "unknown")
            val id = myPreferences.getString("id", "unknown")

            //Guardar Datos
            var boardFactory = InjectorUtils.providerBoardViewModelFactory()
            var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

            boardViewModel.getAllBoardFollow(token).observe(viewLifecycleOwner, Observer {
                results ->
                for(result in results){
                    if(result[1]== id && result[2] == idBoard){
                        idFollowBoard = result[0]
                        break
                    }
                }

                if (idFollowBoard == "0"){ followBoard.text = "Seguir" }
                else{followBoard.text = "Dejar de Seguir"}

                //Cargar  funcionalidad
                loadFutionalityFollowButton()
            })

        }
    }


    fun loadFutionalityFollowButton(){
        followBoard.setOnClickListener {
            val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val token = myPreferences.getString("token", "unknown")
            val id = myPreferences.getString("id", "unknown")

            //Guardar Datos
            var boardFactory = InjectorUtils.providerBoardViewModelFactory()
            var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

            if(idFollowBoard == "0"){
                boardViewModel!!.addUserFollowBoard(token, id, idBoard).observe(viewLifecycleOwner, Observer {
                    result ->
                    if(result > 0){
                        idFollowBoard = result.toString()
                        followBoard.text = "Dejar de Seguir"
                    }
                    else{activity?.finish()}

                })
            }
            else{
                boardViewModel!!.deleteUserFollowBoard(token, idFollowBoard).observe(viewLifecycleOwner, Observer {
                    result ->
                    if(result > 0){
                        idFollowBoard = "0"
                        followBoard.text = "Seguir"
                    }
                    else{activity?.finish()}
                })
            }
        }

    }


    fun loadButtonEdit(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")
        if(id == board.idUsuario){
            editBoard.visibility = View.VISIBLE
        }
        else{
            editBoard.visibility = View.GONE
        }
    }

    fun loadButtondelete(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")
        if(id == board.idUsuario){
            deleteBoard.visibility = View.VISIBLE
        }
        else{
            deleteBoard.visibility = View.GONE
        }
    }

}

