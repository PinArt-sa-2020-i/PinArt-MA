package com.example.pinart_ma.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.TagRepository

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

    fun getAllTags(token: String?) = tagRepository.getAllTags(token)
}