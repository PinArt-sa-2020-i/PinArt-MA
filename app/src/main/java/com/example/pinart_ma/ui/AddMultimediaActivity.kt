package com.example.pinart_ma.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pinart_ma.R
import kotlinx.android.synthetic.main.activity_add_multimedia.*
import java.io.File

class AddMultimediaActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val tagList: MutableList<String> = mutableListOf()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_multimedia)

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

        addButton.setOnClickListener {
            if(addTag.text.isNotEmpty()){
                tagList.add(addTag.text.toString())
                tagsText.text = tagList.distinctBy{it.toUpperCase()}.toString()
                addTag.text.clear()
            }else{
                Toast.makeText(this, "Agrega un Tag", Toast.LENGTH_SHORT).show()
            }
        }

        saveImage.setOnClickListener {
            if(description.text.isNotEmpty() and tagList.isNotEmpty() and img_pick_btn.text.equals("Image Selected")){
                finish()
            }
        }

        //initializeUi()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
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
            img_pick_btn.text = "Image Selected"
            image_view.setImageURI(data?.data)
        }
    }

}