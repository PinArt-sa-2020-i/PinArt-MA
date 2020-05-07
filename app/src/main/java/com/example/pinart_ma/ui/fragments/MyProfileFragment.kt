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
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.AddMultimediaActivity
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_my_profile.*
import android.graphics.Color
import com.example.pinart_ma.ui.ConfigurationActivity
import com.example.pinart_ma.ui.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuration.*

class MyProfileFragment: Fragment() {

    companion object {
        fun newInstance(): MyProfileFragment = MyProfileFragment()
    }

    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        loadUser(context)
        loadFollow(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMultimediaFragment()

        loadLogOutButton(context)

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
                    user = User(0, "Bot-001", "Boot", "001", null, null, null, null, null, null, null)
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
        if(user.foto == null){Picasso.get().load("https://uwosh.edu/deanofstudents/wp-content/uploads/sites/156/2019/02/profile-default.jpg").into(imageMyProfile)}
        else{Picasso.get().load(user.foto).into(imageMyProfile) }
        //myProfileFollowers.text = "? seguidores"
        //myProfileFollowing.text = "? siguiendo"
    }



    private fun loadMultimediaFragment() {
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.containerMyProfileFragment, ProfileMultimediaFragment.newInstance(id, true))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun loadFollow(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id = myPreferences.getString("id", "unknown")
        val token = myPreferences.getString("token", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getAllUserFollow(token).observe(viewLifecycleOwner, Observer {
            userFollows ->
            var followers: Int = 0
            var following: Int = 0
            for (i in 0 until userFollows.size){
                if(userFollows[i][2] == id){ followers++}
                if(userFollows[i][1] == id){ following++}
            }
            myProfileFollowers.text = "$followers seguidores"
            myProfileFollowing.text = "$following siguiendo"
        })
    }


    fun loadLogOutButton(context: Context?){
            cerrarSesionConfigurations.setOnClickListener {
                val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                val myEditor = myPreferences.edit()
                myEditor.clear()
                myEditor.commit();

                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                activity?.finish()
        }


    }
}