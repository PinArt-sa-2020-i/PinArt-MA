package com.example.pinart_ma.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.adapter.ViewImageTagsAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.TagViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register_add_tags.*
import kotlinx.android.synthetic.main.activity_view_image_activity.*
import kotlinx.android.synthetic.main.list_item_feed.view.*

class ViewImageActivity() : AppCompatActivity() {

    lateinit var idMultimedia: String
    lateinit var multimedia : Multimedia
    lateinit var user: User
    var tags : ArrayList<Tag> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image_activity)
        loadMultimedia()
    }

    fun loadMultimedia(){
        idMultimedia = intent.getStringExtra("idMultimedia")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(MultimediaViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var token: String? = myPreferences.getString("token", "unknown")

        multimediaViewModel!!.getMultimediaById(token, idMultimedia).observe(this, Observer {
            multimediaAux ->
            if(multimediaAux == null){finish()}
            else{
                multimedia = multimediaAux
                Picasso.get().load(multimediaAux.url).into(imageViewViewImage);
                descripcionTextViewViewImage.text = multimediaAux.descripcion

                // Carga los Datos del usuario
                loadUser(multimediaAux.idUsuarioCreador)
                //Carga las etiquetas
                loadTags(multimediaAux.etiquetasRelacionadas)
            }
        })
    }

    fun loadUser(idUsuario: String){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id = myPreferences.getString("id", "unknown")
        var token: String? = myPreferences.getString("token", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getUserById(token, idUsuario).observe(this, Observer {
            userData ->
            if(userData==null){
                finish()
            }
            else{
                user = userData
                userNameTextViewViewImage.text = userData.username

                if (id == idUsuario){
                    userNameTextViewViewImage.setOnClickListener {
                        var intent: Intent = Intent(this,  MainActivity::class.java)
                        intent.putExtra("typeFragment", "myProfileFragment")
                        startActivity(intent)
                    }
                    loadFuntionalityDeleteButton()


                }else{
                    userNameTextViewViewImage.setOnClickListener {
                        var intent: Intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("typeFragment", "otherProfileFragment")
                        intent.putExtra("idUsuario", idUsuario)
                        startActivity(intent)
                    }
                }

            }
        })

    }

    fun loadTags(tagsIds: ArrayList<String>?){

        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var token: String? = myPreferences.getString("token", "unknown")

        tagViewModel!!.getAllTags(token).observe(this, Observer {
            tagsListResponse ->
            for(i in 0 until  tagsListResponse.size){
                if (tagsIds != null) {
                    if(tagsIds.contains(tagsListResponse[i].id)){
                        tags.add(tagsListResponse[i])
                    }
                }
            }

            //Se llama al recyler view
            containerRecyclerViewViewImage.layoutManager = GridLayoutManager(this,3)

            containerRecyclerViewViewImage.adapter = ViewImageTagsAdapter(tags)
        })
    }


    fun loadFuntionalityDeleteButton(){
        buttonEliminarImagen.visibility = View.VISIBLE
        idMultimedia = intent.getStringExtra("idMultimedia")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(MultimediaViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var token: String? = myPreferences.getString("token", "unknown")

        buttonEliminarImagen.setOnClickListener {
            multimediaViewModel!!.deleteMultimediaById(token, idMultimedia).observe(this, Observer {
                resultado ->
                if(resultado == 0){finish()}
                else{
                    var intent: Intent = Intent(this,  MainActivity::class.java)
                    intent.putExtra("typeFragment", "myProfileFragment")
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent)
                    finish();
                }
            })
        }

    }
}