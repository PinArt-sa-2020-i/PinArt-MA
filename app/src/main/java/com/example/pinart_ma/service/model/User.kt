package com.example.pinart_ma.service.model

import com.google.gson.annotations.SerializedName

class User( var id: Int,
            var username: String,
            var firstname: String?,
            var lastname: String?,
            var correo: String?,
            var token: String?,
            var genero: String?,
            var foto: String?,
            var descripcion: String?,
            var noTelefono: String?,
            var edad: String?,
            var idSesion: Int?
){
}