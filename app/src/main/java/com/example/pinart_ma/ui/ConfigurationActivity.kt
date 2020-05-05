package com.example.pinart_ma.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pinart_ma.R
import kotlinx.android.synthetic.main.activity_configuration.*

class ConfigurationActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        logOut.setOnClickListener {
            val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val myEditor = myPreferences.edit()
            myEditor.clear()
            myEditor.commit();

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
}