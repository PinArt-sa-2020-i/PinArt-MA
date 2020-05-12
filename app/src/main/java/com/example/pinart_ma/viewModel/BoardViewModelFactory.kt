package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinart_ma.service.repository.BoardRepository

class BoardViewModelFactory(private val boardRepository: BoardRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        return BoardViewModel(boardRepository) as T
    }

}
