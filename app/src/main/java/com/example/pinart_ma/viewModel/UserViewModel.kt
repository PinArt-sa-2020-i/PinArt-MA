package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.UserRepository

class UserViewModel(private val userRepository : UserRepository) : ViewModel() {

    fun registerUser(firstName: String, lastName: String, username: String, password: String, correo:String)
            = userRepository.registerUser(firstName, lastName, username, password, correo )

    fun authenticateUser(username: String, password: String) = userRepository.authenticateUser(username, password)

    fun getAllUsers(token: String?) = userRepository.getAllUsers(token)

    fun getUserById(token: String?, idUsuario: String?) = userRepository.getUserById(token, idUsuario)

    fun getAllUserFollow(token: String?) = userRepository.getAllUserFollow(token)

    fun deleteUserFollow(token: String?, idFollow: String?) = userRepository.deleteUserFollow(token, idFollow)

    fun createUserFollow(token: String?, idFollower: String?, idFollowing: String?) = userRepository.createUserFollow(token, idFollower, idFollowing)

    fun updateUserProfile(token: String?, idUser:String?, foto: String?, descripcion: String, noTelefono: String, edad: String,genero: String)
            = userRepository.updateUserProfile(token, idUser, foto, descripcion, noTelefono, edad, genero)

    fun deleteSesion(token: String?, idSesion: String?) =  userRepository.deleteSesion(token, idSesion)
}