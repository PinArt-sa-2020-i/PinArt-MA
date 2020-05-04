package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.content.Intent
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
import com.example.pinart_ma.ui.AddMultimediaActivity
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.my_profile_fragment.*
import android.graphics.Color
import com.example.pinart_ma.ui.ConfigurationActivity

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

        myProfileUploadMultimedia.setOnClickListener {
            var intent = Intent(activity, AddMultimediaActivity::class.java)
            startActivity(intent)
        }

        myProfileConfigurations.setOnClickListener {
            var intent = Intent(activity, ConfigurationActivity::class.java)
            startActivity(intent)
        }

        loadMultimediaFragment()
        myProfileMultimedia.setBackgroundResource(R.drawable.rounded_button_feed)
        myProfileMultimedia.setTextColor(Color.parseColor("#FFFFFF"))

        myProfileMultimedia.setOnClickListener{
            myProfileMultimedia.setBackgroundResource(R.drawable.rounded_button_feed)
            myProfileMultimedia.setTextColor(Color.parseColor("#FFFFFF"))
            myProfileTableros.setBackgroundColor(Color.TRANSPARENT)
            myProfileTableros.setTextColor(Color.parseColor("#000000"))
        }

        myProfileTableros.setOnClickListener{
            myProfileTableros.setBackgroundResource(R.drawable.rounded_button_feed)
            myProfileTableros.setTextColor(Color.parseColor("#FFFFFF"))
            myProfileMultimedia.setBackgroundColor(Color.TRANSPARENT)
            myProfileMultimedia.setTextColor(Color.parseColor("#000000"))
        }
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
        nameMyProfile.text = (user.firstname + " " + user.lastname)
        userNameMyProfile.text = user.username
        myProfileFollowers.text = "? seguidores"
        myProfileFollowing.text = "? siguiendo"
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