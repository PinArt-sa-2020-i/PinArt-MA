package com.example.pinart_ma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import com.example.pinart_ma.viewModel.UserViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var userFactory: UserViewModelFactory? = null
    var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUi()
    }


    private fun initializeUi() {
        userFactory = InjectorUtils.provideUserViewModelFactory()

        userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.registerUser("Camilo", "Gil", "Pepe30", "12345678").observe(this, Observer {
            id ->
            Log.d("TAG", "Hola $id")
            texto.text = id.toString()
        })
    }
}
