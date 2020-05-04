package com.example.pinart_ma.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.TagViewModel
import kotlinx.android.synthetic.main.activity_add_multimedia.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color


class AddMultimediaActivity: AppCompatActivity() {

    var  fileX: File? = null
    val selectedTags: MutableList<String> = mutableListOf()
    val checkBoxList: MutableList<CheckBox> = mutableListOf()
    val tagList: MutableList<String> = mutableListOf()
    val tagListIds: MutableMap<String, String> = mutableMapOf()
    
    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_multimedia)

        image_view.clipToOutline = true
        
        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)


        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var token: String? = myPreferences.getString("token", "unknown")

        tagViewModel.getAllTags(token).observe(this, Observer { 
                tagListObserver ->
                for (i in 0 until tagListObserver.size){
                    tagList.add(tagListObserver[i].name)
                    tagListIds.put(tagListObserver[i].name, tagListObserver[i].id)
                }
                loadFuntionality()
        })
        
        
        

        //initializeUi()
    }

    override fun onStart() {
        super.onStart()
        
    }
    
    fun loadFuntionality(){
        for (i in 0 until tagList.size) {
            val ch = CheckBox(this)
            ch.text = tagList[i]
            checkBoxList.add(ch)
            tagsRow.addView(checkBoxList[i])
        }

        img_pick_btn.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                }else{
                    pickImageFromGallery();
                }
            }else{
                pickImageFromGallery();
            }
        }

        saveImage.setOnClickListener {
            if(description.text.isNotEmpty() and tagList.isNotEmpty() and img_pick_btn.text.equals("Cambiar imagen")){
                for(checkBox in checkBoxList){
                    if(checkBox.isChecked){
                        selectedTags.add(checkBox.text.toString())
                    }
                }

                Toast.makeText(this, selectedTags.toString(), Toast.LENGTH_SHORT).show()
                addMultimedia()

            }else{
                Toast.makeText(this, "Informacion incompleta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    //Handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            getFile(data)
            img_pick_btn.text = "Cambiar imagen"
            img_pick_btn.setBackgroundResource(R.drawable.rounded_search)
            img_pick_btn.setTextColor(Color.parseColor("#000000"))
            image_view.setImageURI(Uri.fromFile(fileX))

            saveImage.setBackgroundResource(R.drawable.rounded_followbutton)
            saveImage.setTextColor(Color.parseColor("#FFFFFF"))
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
    
    fun addMultimedia(){
        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java)
        
        multimediaViewModel!!.upLoadFileBucket(fileX).observe(this, Observer {
                idBucket ->
                if(idBucket == null){}
                else{
                    val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                    
                    var id: String? = myPreferences.getString("id", "unknown")
                    var descripcion: String? = description.text.toString()
                    var idEtiquetas: ArrayList<String?> = arrayListOf()
                    var url_imagen: String? = "https://pinart-images-storage.s3.amazonaws.com/"+idBucket
                    var formato: String? = "JPG"
                    var tamano: String? = "Tamano"
                    
                    for (i in 0 until selectedTags.size){
                        idEtiquetas.add(tagListIds[selectedTags[i]])
                    }

                    selectedTags as ArrayList<String>
                    
                    multimediaViewModel!!.addMultimedia(id, descripcion, idEtiquetas, url_imagen, formato, tamano, idBucket).observe(this, Observer { 
                        result ->
                        Log.d("TAG", result.toString())
                    })
                }
            
        })
      
        /*
        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id: String? = myPreferences.getString("id", "unknown")
        var token: String? = myPreferences.getString("token", "unknown")


        multimediaViewModel!!.getFeedTags(token, id).observe(viewLifecycleOwner, Observer {
                multimediaList ->
                for(i in 0 until multimediaList.size){
                    feed.add(multimediaList[i])
                }
                Toast.makeText(context, feed.size.toString(), Toast.LENGTH_SHORT).show()
                recyclerViewFeedUsers.layoutManager=  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerViewFeedUsers.adapter = TagsFeedAdapter(feed)

        })
         */
    }

    

}