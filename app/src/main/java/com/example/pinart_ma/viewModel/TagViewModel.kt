package com.example.pinart_ma.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.TagRepository

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

    fun getAllTags(token: String?) = tagRepository.getAllTags(token)

    fun getTagById(token: String?, idTag:String?) = tagRepository.getTagById(token, idTag)

    fun getTagFolledByUser(token: String?, idUsuario: String?) = tagRepository.getTagFolledByUser(token, idUsuario)

    fun followTag(token: String?, idUsuario: String?, idTag: String?) = tagRepository.followTag(token, idUsuario, idTag)
}