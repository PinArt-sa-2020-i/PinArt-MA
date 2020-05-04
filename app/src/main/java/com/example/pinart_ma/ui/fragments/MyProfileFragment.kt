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
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.my_profile_fragment.*

class MyProfileFragment: Fragment() {

    companion object {
        fun newInstance(): MyProfileFragment = MyProfileFragment()
    }

    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.my_profile_fragment, container, false)
        loadUser(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMultimediaFragment()

    }


    fun loadUser(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")
        val token = myPreferences.getString("token", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getUserById(token, id).observe(viewLifecycleOwner, Observer {
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
        userNameMyProfile.text = user.username
        firstNameMyProfile.text = user.firstname
        lastNameMyProfile.text = user.lastname
    }



    private fun loadMultimediaFragment() {
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.containerMyProfileFragment, ProfileMultimediaFragment.newInstance(id, true))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}