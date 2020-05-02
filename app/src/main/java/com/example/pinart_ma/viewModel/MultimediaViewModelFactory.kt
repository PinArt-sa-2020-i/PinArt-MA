package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinart_ma.service.repository.MultimediaRepository

class MultimediaViewModelFactory(private val multimediaRepository: MultimediaRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        return MultimediaViewModel(multimediaRepository) as T
    }
}
