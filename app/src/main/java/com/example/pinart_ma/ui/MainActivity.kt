package com.example.pinart_ma.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.fragments.FeedFragment
import com.example.pinart_ma.ui.fragments.FeedUsersFragment
import com.example.pinart_ma.ui.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load Feed por default
        val fragment = FeedFragment.newInstance()
        openFragment(fragment)

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
