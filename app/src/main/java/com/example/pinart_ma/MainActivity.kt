package com.example.pinart_ma

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //var userFactory: UserViewModelFactory? = null
    //var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        val name = myPreferences.getString("id", "unknown")


        textoMain.text = name.toString()

        //initializeUi()
    }

    /*
    private fun initializeUi() {
        userFactory = InjectorUtils.provideUserViewModelFactory()

        userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.registerUser("Camilo", "Gil", "Pepe30", "12345678").observe(this, Observer {
            id ->
            Log.d("TAG", "Hola $id")
            texto.text = id.toString()
        })
    }
    */
}
