package com.example.pinart_ma.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pinart_ma.R
import kotlinx.android.synthetic.main.activity_add_multimedia.*


class AddMultimediaActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val tagList: MutableList<String> = mutableListOf("pepe", "papo", "papa","pipo", "pipu", "destructor", "masticadores", "esternocledomastoideo")
        val checkBoxList: MutableList<CheckBox> = mutableListOf()
        val selectedTags: MutableList<String> = mutableListOf()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_multimedia)

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
            if(description.text.isNotEmpty() and tagList.isNotEmpty() and img_pick_btn.text.equals("Image Selected")){
                for(checkBox in checkBoxList){
                    if(checkBox.isChecked){
                        selectedTags.add(checkBox.text.toString())
                    }
                }

                Toast.makeText(this, selectedTags.toString(), Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Informacion incompleta", Toast.LENGTH_SHORT).show()
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