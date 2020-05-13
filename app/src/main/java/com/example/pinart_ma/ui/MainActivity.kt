package com.example.pinart_ma.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {



    var numFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().postDelayed(Runnable {
            //----------------------------
            //animacionCarga.visibility = View.GONE
            animacionCarga.animate().alpha(0.0f);
            //----------------------------
        }, 1000)


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
                else -> {
                    return SearchInitFragment.newInstance()
                }
            }
    }

    /*
    val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        val name = myPreferences.getString("id", "unknown")
     */
}
