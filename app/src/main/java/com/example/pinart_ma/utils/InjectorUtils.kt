package com.example.pinart_ma.utils

import com.example.pinart_ma.service.repository.BoardRepository
import com.example.pinart_ma.service.repository.MultimediaRepository
import com.example.pinart_ma.service.repository.TagRepository
import com.example.pinart_ma.service.repository.UserRepository
import com.example.pinart_ma.viewModel.BoardViewModelFactory
import com.example.pinart_ma.viewModel.MultimediaViewModelFactory
import com.example.pinart_ma.viewModel.TagViewModelFactory
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

    fun providerTagViewModelFactory(): TagViewModelFactory {
        val tagRepository = TagRepository.getInstance()
        return TagViewModelFactory(tagRepository)
    }

    fun providerBoardViewModelFactory(): BoardViewModelFactory {
        val boardRepository = BoardRepository.getInstance()
        return BoardViewModelFactory(boardRepository)
    }

}