package com.example.pinart_ma.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.fragments.*
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.FCMViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {



    var numFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sincronizarTopics();


        animacionCarga.visibility = View.GONE
        /*
        Handler().postDelayed(Runnable {
            //----------------------------
            //animacionCarga.visibility = View.GONE
            animacionCarga.animate().alpha(0.0f);
            //----------------------------
        }, 1000)
        */

        //Se obtienen el fragment por default
        var defaultFragment = recuperarFragment(intent)

        // Load Feed por default
        openFragment(defaultFragment)

        bottom_navigation.itemIconTintList = null;

        bottom_navigation.setOnNavigationItemSelectedListener {
                menuItem ->
                when(menuItem.itemId){
                    R.id.navigation_feed ->{
                        val fragment = FeedFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.navigation_busqueda -> {
                        val fragment = SearchFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.navigation_profile ->{
                        val fragment = MyProfileFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    else
                        -> false
                }

        }
    }


    override fun onBackPressed() {
        numFragment--
        if(numFragment == 0){
            finish()
        }
        else{
            super.onBackPressed() // Invoca al método
        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        numFragment++
    }


    private fun recuperarFragment(intent: Intent) : Fragment{
            when(intent.getStringExtra("typeFragment")){
                "feedFragment" -> {
                    return FeedFragment.newInstance()
                }
                "otherProfileFragment" ->{
                    return OtherProfileFragment.newInstance(intent.getStringExtra("idUsuario"))
                }
                "tagFragment" -> {
                    return TagFragment.newInstance(intent.getStringExtra("idTag"))
                }
                "myProfileFragment" -> {
                    return MyProfileFragment.newInstance()
                }
                "boardFragment" ->{
                    return BoardFragment.newInstance(intent.getStringExtra("idBoard"))
                }
                "followsFragment" ->{
                    return FollowsFragment.newInstance(intent.getStringExtra("typeList"))
                }
                else -> {
                    return SearchInitFragment.newInstance()
                }
            }
    }


    fun testSuscriber(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val token_fire_base = myPreferences.getString("toke-fire-base", "unknown")

        var fcmFactory = InjectorUtils.providerFCMViewModelFactory()
        var fcmViewModel = ViewModelProviders.of(this, fcmFactory).get(FCMViewModel::class.java);

    }



    fun sincronizarTopics(){
        Log.d("TAG-E", "HOLIS :3")
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val token_fire_base = myPreferences.getString("toke-fire-base", "unknown")
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        var fcmFactory = InjectorUtils.providerFCMViewModelFactory()
        var fcmViewModel = ViewModelProviders.of(this, fcmFactory).get(FCMViewModel::class.java);

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);


        fcmViewModel.getTopicsSuscriber(token_fire_base).observe(this, Observer { topics ->
            var followings: ArrayList<String> = arrayListOf()

            userViewModel.getAllUserFollow(token).observe(this, Observer { results ->

                for(result in results){
                    if(result[1] == id){followings.add(result[2])}
                }
                //TODO
                for(following in followings ){
                    if(!topics.contains(following)){
                        fcmViewModel!!.suscriberTopic(following, token_fire_base).observe(this, Observer { result -> })
                    }
                }

                for (topic in topics){
                    if (!followings.contains(topic)){
                        fcmViewModel!!.unsuscriberTopic(topic, token_fire_base).observe(this, Observer { result -> })
                    }
                }

            })
        })
    }
}
