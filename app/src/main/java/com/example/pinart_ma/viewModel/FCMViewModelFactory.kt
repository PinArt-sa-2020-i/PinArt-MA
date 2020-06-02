package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinart_ma.service.repository.FCMRepository


class FCMViewModelFactory(private val fcmRepository: FCMRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        return FCMViewModel(fcmRepository) as T
    }

}
