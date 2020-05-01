package com.example.pinart_ma.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegisterRegister.setOnClickListener {
            registerUser()
        }

        buttonLoginRegister.setOnClickListener {
            onBackPressed();
            //startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    fun registerUser(){
        val firstName: String = firstNameRegister.text.toString()
        val lastName: String = lastNameRegister.text.toString()
        val userName: String = usernameRegister.text.toString()
        val correo: String = correoRegister.text.toString()
        val password: String = passwordRegister.text.toString()

        /*
        when{
            TextUtils.isEmpty(firstName) -> Toast.makeText(this,  "Este campo es requerido", Toast.LENGTH_SHORT)
        }
        */

        var userFactory = InjectorUtils.provideUserViewModelFactory()

        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.registerUser(firstName, lastName, userName, password, correo).observe(this, Observer {
                id ->
                Log.d("TAG", id.toString())
                if(id > 0){
                    Toast.makeText(this, "Registro Correcto: $id", Toast.LENGTH_LONG).show()
                    onBackPressed();
                }
                else{
                    Toast.makeText(this, "Error: Registro Fallido", Toast.LENGTH_SHORT).show()
                }
        })
    }
}
