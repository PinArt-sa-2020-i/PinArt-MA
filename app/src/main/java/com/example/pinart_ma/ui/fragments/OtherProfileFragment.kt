package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.my_profile_fragment.*
import kotlinx.android.synthetic.main.other_profile_fragment.*

class OtherProfileFragment(var idUsuario: String): Fragment() {
    companion object {
        fun newInstance(idUsuario: String): OtherProfileFragment = OtherProfileFragment(idUsuario)
    }

    lateinit var user: User
    var idFollow: String = "0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.other_profile_fragment, container, false)
        loadUser(context)
        loadFollowButton(context)
        loadMultimediaFragment()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seguirButtonOtherProfile.setOnClickListener { loadFuntionlityFollowButton(context) }
    }

    fun loadUser(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getUserById(token, idUsuario).observe(viewLifecycleOwner, Observer {
                userData ->
            if(userData==null){
                user = User(0, "Bot-001", "Boot", "001", null, null)
                mostrarInfoUsuario(context)
            }
            else{
                user = userData
                mostrarInfoUsuario(context)
            }
        })

    }

    fun mostrarInfoUsuario(context: Context?){
        userNameTextViewOtherProfile.text = user.username
        firstNameTextViewOtherProfile.text = user.firstname
        lastNameTextViewOtherProfile.text = user.lastname
    }

    private fun loadMultimediaFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.containerOtherProfileFragment, ProfileMultimediaFragment.newInstance(idUsuario, false))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun loadFollowButton(context: Context?){

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val idUsuarioPerfil = myPreferences.getString("id", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getAllUserFollow(token).observe(viewLifecycleOwner, Observer {
            follows ->
            var isFollow : Boolean = false
            for(i in 0 until  follows.size){
                Log.d("TAG", "$follows[i][1]  $follows[i][2]")
                if( follows[i][1] == idUsuarioPerfil && follows[i][2] == idUsuario){
                    isFollow = true
                    idFollow = follows[i][0]
                    break
                }
            }

            if(isFollow == true){
                seguirButtonOtherProfile.text = "Dejar de seguir usuario"
            }
            else{
                seguirButtonOtherProfile.text = "Seguir usuario"
            }
        })
    }

    fun loadFuntionlityFollowButton(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val idUsuarioPerfil = myPreferences.getString("id", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        if(idFollow == "0"){
            userViewModel!!.createUserFollow(token, idUsuarioPerfil, idUsuario).observe(viewLifecycleOwner, Observer {
                response ->
                if (response == 0){}
                else{
                    idFollow = response.toString()
                    seguirButtonOtherProfile.text = "Dejar de seguir usuario"
                }
            })
        }
        else{
            userViewModel!!.deleteUserFollow(token, idFollow).observe(viewLifecycleOwner, Observer {
                response ->
                if (response == 0){}
                else{
                    idFollow = "0"
                    seguirButtonOtherProfile.text = "Seguir usuario"
                }
            })
        }
    }

}