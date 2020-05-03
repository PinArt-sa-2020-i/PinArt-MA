package com.example.pinart_ma.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
