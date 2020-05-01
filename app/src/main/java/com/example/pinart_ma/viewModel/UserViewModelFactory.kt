package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinart_ma.service.repository.UserRepository

class UserViewModelFactory(private val  userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }

}