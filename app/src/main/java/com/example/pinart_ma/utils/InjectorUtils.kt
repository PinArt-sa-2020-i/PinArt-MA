package com.example.pinart_ma.utils

import com.example.pinart_ma.service.repository.MultimediaRepository
import com.example.pinart_ma.service.repository.UserRepository
import com.example.pinart_ma.viewModel.MultimediaViewModelFactory
import com.example.pinart_ma.viewModel.UserViewModelFactory

object InjectorUtils {
    fun provideUserViewModelFactory(): UserViewModelFactory {
        val userRepository = UserRepository.getInstance()
        return UserViewModelFactory(userRepository)
    }

    fun providerMultimediaViewModelFactory(): MultimediaViewModelFactory{
        val multimediaRepository = MultimediaRepository.getInstance()
        return MultimediaViewModelFactory(multimediaRepository)
    }
}