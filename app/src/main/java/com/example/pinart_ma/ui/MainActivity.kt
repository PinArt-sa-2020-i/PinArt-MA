package com.example.pinart_ma.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.fragments.FeedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottom_navigation.setOnNavigationItemSelectedListener {
                menuItem ->
                when(menuItem.itemId){
                    R.id.navigation_feed ->{
                        val fragment = FeedFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.navigation_busqueda -> {
                        texto.text = "Busqueda"
                        true
                    }
                    R.id.navigation_profile ->{
                        texto.text = "Profile"
                        true
                    }
                    else
                        -> false
                }

        }


        //initializeUi()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    /*

    val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        val name = myPreferences.getString("id", "unknown")

     */
}
