package com.example.pinart_ma.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.TagViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_add_tags.*

class RegisterAddTagsActivity : AppCompatActivity(){

    var selectTags : ArrayList<Boolean> = arrayListOf()
    var idsTags : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_add_tags)

        mapEtiquetas(this)
        addFuntionalityButton(this)
        loginUser(this, intent.getStringExtra("userName"), intent.getStringExtra("password"))

        nextRegisterAddTags.setOnClickListener {
            if(addEtiquetas(this)==true){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("typeFragment", "feedFragment")
                startActivity(intent)
            }
        }

    }

    fun mapEtiquetas(context: Context?){
        selectTags = arrayListOf(
            false, false, false,
            false, false, false,
            false, false, false,
            false, false, false)

        idsTags = arrayListOf(
            "12", "13", "14",
            "15", "16", "17",
            "18", "19", "20",
            "21", "22", "23"
        )
    }

    fun addFuntionalityButton(context: Context?){

        viajesTagRegisterAddTags.setOnClickListener {
            selectTags[0] = !selectTags[0]
            if(selectTags[0] == true){
                viajesTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                viajesTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        recetasTagRegisterAddTags.setOnClickListener {
            selectTags[1] = !selectTags[1]
            if(selectTags[1] == true){
                recetasTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                recetasTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        deportesTagRegisterAddTags.setOnClickListener {
            selectTags[2] = !selectTags[2]
            if(selectTags[2] == true){
                deportesTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                deportesTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        noticiasTagRegisterAddTags.setOnClickListener {
            selectTags[3] = !selectTags[3]
            if(selectTags[3] == true){
                noticiasTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                noticiasTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        musicaTagRegisterAddTags.setOnClickListener {
            selectTags[4] = !selectTags[4]
            if(selectTags[4] == true){
                musicaTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                musicaTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        peliculasSeriesTagRegisterAddTags.setOnClickListener {
            selectTags[5] = !selectTags[5]
            if(selectTags[5] == true){
                peliculasSeriesTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                peliculasSeriesTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        humorTagRegisterAddTags.setOnClickListener {
            selectTags[6] = !selectTags[6]
            if(selectTags[6] == true){
                humorTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                humorTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        gamesTagRegisterAddTags.setOnClickListener {
            selectTags[7] = !selectTags[7]
            if(selectTags[7] == true){
                gamesTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                gamesTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        paisajesTagRegisterAddTags.setOnClickListener {
            selectTags[8] = !selectTags[8]
            if(selectTags[8] == true){
                paisajesTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                paisajesTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        autosTagRegisterAddTags.setOnClickListener {
            selectTags[9] = !selectTags[9]
            if(selectTags[9] == true){
                autosTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                autosTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        fitnessTagRegisterAddTags.setOnClickListener {
            selectTags[10] = !selectTags[10]
            if(selectTags[10] == true){
                fitnessTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                fitnessTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }

        modaTagRegisterAddTags.setOnClickListener {
            selectTags[11] = !selectTags[11]
            if(selectTags[11] == true){
                modaTagRegisterAddTags.setBackgroundColor(Color.rgb(72, 201, 176 ))
            }
            else{
                modaTagRegisterAddTags.setBackgroundColor(Color.rgb(209, 242, 235 ))
            }
        }


    }

    fun loginUser(context: Context?, userName: String, password : String){
        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)

        userViewModel!!.authenticateUser(userName, password).observe(this, Observer {
                user ->
            if(user==null){
                finish()
                startActivity(Intent(context, LoginActivity::class.java))
            }
            else{
                saveDataCacheFake("id", user.id.toString())
                saveDataCacheFake("token", user.token.toString())
            }
        })
    }

    fun saveDataCacheFake(key:String, data:String){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putString(key, data);
        myEditor.commit();
    }

    fun verificarEtiquetas(): Boolean{
        for (i in 1 until selectTags.size){
            if (selectTags[i] == true){
                return true
            }
        }
        return false
    }

    fun addEtiquetas(context: Context?): Boolean{
        if (verificarEtiquetas() == true){
            for(i in 0 until selectTags.size){
                if (selectTags[i] == true){
                    addEtiqueta(context, i)
                }
            }
            return true
        }
        else{
            Toast.makeText(this, "Selecciona una etiqueta almenos", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun addEtiqueta(context: Context?, i: Int){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id = myPreferences.getString("id", "unknown")
        val token = myPreferences.getString("token", "unknown")

        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        tagViewModel!!.followTag(token, id, idsTags[i]).observe(this, Observer {
            resultado ->
        })
    }

}