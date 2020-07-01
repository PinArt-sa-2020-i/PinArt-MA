package com.example.pinart_ma.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_multimedia.*
import kotlinx.android.synthetic.main.activity_configuration.*
import kotlinx.android.synthetic.main.list_item_feed.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class ConfigurationActivity: AppCompatActivity()  {

    lateinit var user: User
    lateinit var fileX: File

    companion object{
        val IMAGE_PICK_CODE = 1000
        val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        loadUser()
        loadDescartarCambios()
        loadFuntionalityButtonUpload()
        loadLogOutButton()

    }


    fun loadUser(){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id = myPreferences.getString("id", "unknown")
        val token = myPreferences.getString("token", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel!!.getUserById(token, id).observe(this, Observer {
                userData ->
            if(userData==null){
                user = User(0, "Bot-001", "Boot", "001", null, null, null, null, null, null, null, null)
            }
            else{
                user = userData
            }
            mostrarInformacion()
        })
    }

    fun mostrarInformacion(){
        if (user.username == null){ userNameConfiguration.text = "NotFound"}
        else{ userNameConfiguration.text = user.username}

        if (user.firstname == null){ firstNameConfiguration.text = "NotFound"}
        else{ firstNameConfiguration.text = user.firstname}

        if (user.lastname == null){ lastNameConfiguration.text = "NotFound"}
        else{ lastNameConfiguration.text = user.lastname}

        if (user.correo == null){ correoConfiguration.text = "NotFound"}
        else{ correoConfiguration.text = user.correo}

        if (user.genero == null){ generoConfiguration.hint = ("Genero")}
        else{ generoConfiguration.setText(user.genero)}

        if (user.descripcion == null){ descripcionConfiguration.hint = ("Descripcion")}
        else{ descripcionConfiguration.setText(user.descripcion)}

        if (user.noTelefono == null){ telefonoConfiguration.hint = ("Telefono")}
        else{ telefonoConfiguration.setText(user.noTelefono)}
        edadConfiguration

        if (user.edad == null){ edadConfiguration.hint = "Edad"}
        else{ edadConfiguration.setText(user.edad)}

        if(user.foto == null){Picasso.get().load("https://uwosh.edu/deanofstudents/wp-content/uploads/sites/156/2019/02/profile-default.jpg").into(imagenProfile);}
        else{Picasso.get().load(user.foto).into(imagenProfile)}
    }

    fun loadDescartarCambios(){
        descartarConfiguration.setOnClickListener {
            finish()
        }
    }





    fun loadFuntionalityButtonUpload(){
        loadPhotoConfiguration.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, ConfigurationActivity.PERMISSION_CODE)
                }else{
                    pickImageFromGallery();
                }
            }else{
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ConfigurationActivity.IMAGE_PICK_CODE)
    }


    //Handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            ConfigurationActivity.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission from popup granted
                    pickImageFromGallery()
                }else{
                    //Permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    //Handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode ==  ConfigurationActivity.IMAGE_PICK_CODE){
            getFile(data)
            loadPhotoConfiguration.text = "Cambiar imagen"
            imagenProfile.setImageURI(Uri.fromFile(fileX))
            guardarConfiguration.setOnClickListener {
                loadFuntionalitySaveChanges()
            }
        }
    }

    fun getFile(data: Intent?){
        var bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
        val wrapper= ContextWrapper(applicationContext)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)

        file = File(file,"${UUID.randomUUID()}.jpg")

        val stream: OutputStream = FileOutputStream(file)

        bitmap?.compress(Bitmap.CompressFormat.JPEG,100,stream)

        stream.flush()
        stream.close()

        fileX = File(file.absolutePath)


    }

    fun loadFuntionalitySaveChanges(){
        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java)

        multimediaViewModel!!.upLoadFileBucket(fileX).observe(this, Observer { idBucket ->
            if (idBucket == null) {
            } else {
                val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                var id: String? = myPreferences.getString("id", "unknown")
                val token = myPreferences.getString("token", "unknown")
                var url_imagen: String? ="https://pin-art-images-storage.s3.amazonaws.com/" + idBucket


                var userFactory = InjectorUtils.provideUserViewModelFactory()
                var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

                var descripcion = descripcionConfiguration.text.toString()
                var genero = generoConfiguration.text.toString()
                var edad = edadConfiguration.text.toString()
                var noTelefono = telefonoConfiguration.text.toString()

                userViewModel!!.updateUserProfile(token, id, url_imagen,  descripcion, noTelefono, edad, genero).observe(this, Observer {
                        data ->
                    if(data == 1){
                        var intent: Intent = Intent(this,  MainActivity::class.java)
                        intent.putExtra("typeFragment", "myProfileFragment")
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent)
                        finish();
                    }
                    else{
                        finish()
                    }
                })
            }

        })
    }

    fun loadLogOutButton(){
        cerrarSesionConfigurations.setOnClickListener {
            val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)

            var id: String? = myPreferences.getString("id", "unknown")
            val token = myPreferences.getString("token", "unknown")
            val idSesion = myPreferences.getString("idSesion", "unknown")
            val token_fire_base = myPreferences.getString("toke-fire-base", "unknown")

            var userFactory = InjectorUtils.provideUserViewModelFactory()
            var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

            userViewModel!!.deleteSesion(token, idSesion).observe(this, Observer {
                response ->
                val myEditor = myPreferences.edit()
                myEditor.clear()
                myEditor.putString("toke-fire-base", token_fire_base);
                myEditor.commit();

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            })




        }

    }

}