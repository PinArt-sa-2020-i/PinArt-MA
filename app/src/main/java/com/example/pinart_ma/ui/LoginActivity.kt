package com.example.pinart_ma.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyLogin()
        setContentView(R.layout.activity_login)


        buttonRegisterLogin.setOnClickListener {
            startActivity(Intent(this,  RegisterActivity::class.java))
        }

        buttonLoginLogin.setOnClickListener {
            authenticateUser()
        }

    }

    fun verifyLogin(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id = myPreferences.getString("id", "unknown")
        if(id != "unknown"){
            var intent: Intent = Intent(this,  MainActivity::class.java)
            intent.putExtra("typeFragment", "feedFragment")
            startActivity(intent)
            finish();
        }
    }


    fun authenticateUser(){
        var userName = usernameLogin.text.toString()
        var password = passwordLogin.text.toString()

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.authenticateUser(userName, password).observe(this, Observer {
                user ->
                if(user==null){
                    Toast.makeText(this, "Error: Login Fallido", Toast.LENGTH_SHORT).show()
                }
                else{
                    saveDataCacheFake("id", user.id.toString())
                    saveDataCacheFake("idSesion", user.idSesion.toString())
                    saveDataCacheFake("token", user.token.toString())

                    var intent: Intent = Intent(this,  MainActivity::class.java)
                    intent.putExtra("typeFragment", "feedFragment")
                    startActivity(intent)
                    finish();
                }
        })
    }

    fun saveDataCacheFake(key:String, data:String){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putString(key, data);
        myEditor.commit();
    }
}
