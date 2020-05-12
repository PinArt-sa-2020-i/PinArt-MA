package com.example.pinart_ma.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.BoardViewModel
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.activity_create_board.*

class CreateBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        //setFinishOnTouchOutside(false);

        buttonCreateCreateBoard.setOnClickListener {
            createBoard()
        }

        buttonCancelCreateBoard.setOnClickListener {
            finish()
        }
    }

    fun createBoard(){
        //Rescatar datar
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id: String? = myPreferences.getString("id", "unknown")
        var token: String? = myPreferences.getString("token", "unknown")

        var name: String = nameCreateBoard.text.toString()
        var descripcion: String = descriptionCreateBoard.text.toString()

        //Guardar Datos
        var boardFactory =InjectorUtils.providerBoardViewModelFactory()
        var boardViewModel = ViewModelProviders.of(this, boardFactory).get(BoardViewModel::class.java)

        boardViewModel!!.createBoard(token, id, name, descripcion).observe(this, Observer {
            result ->
            if(result<0){
                Toast.makeText(this, "Operacion Fallida", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Tablero Creado", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

    }
}
