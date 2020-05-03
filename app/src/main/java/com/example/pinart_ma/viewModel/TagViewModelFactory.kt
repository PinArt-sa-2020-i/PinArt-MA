package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinart_ma.service.repository.TagRepository

class TagViewModelFactory(private val tagRepository: TagRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        return TagViewModel(tagRepository) as T
    }

}