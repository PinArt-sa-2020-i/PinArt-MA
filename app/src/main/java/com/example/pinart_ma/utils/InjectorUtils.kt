package com.example.pinart_ma.utils

import com.example.pinart_ma.service.repository.*
import com.example.pinart_ma.viewModel.*

object InjectorUtils {
    fun provideUserViewModelFactory(): UserViewModelFactory {
        val userRepository = UserRepository.getInstance()
        return UserViewModelFactory(userRepository)
    }

    fun providerMultimediaViewModelFactory(): MultimediaViewModelFactory{
        val multimediaRepository = MultimediaRepository.getInstance()
        return MultimediaViewModelFactory(multimediaRepository)
    }

    fun providerTagViewModelFactory(): TagViewModelFactory {
        val tagRepository = TagRepository.getInstance()
        return TagViewModelFactory(tagRepository)
    }

    fun providerBoardViewModelFactory(): BoardViewModelFactory {
        val boardRepository = BoardRepository.getInstance()
        return BoardViewModelFactory(boardRepository)
    }

    fun providerFCMViewModelFactory(): FCMViewModelFactory {
        val fcmRepository = FCMRepository.getInstance()
        return FCMViewModelFactory(fcmRepository)
    }

}