package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.UserRepository

class UserViewModel(private val userRepository : UserRepository) : ViewModel() {

    fun registerUser(firstName: String, lastName: String, username: String, password: String, correo:String)
            = userRepository.registerUser(firstName, lastName, username, password, correo )

    fun authenticateUser(username: String, password: String) = userRepository.authenticateUser(username, password)

}