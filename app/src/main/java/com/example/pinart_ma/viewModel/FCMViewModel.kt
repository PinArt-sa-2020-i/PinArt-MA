package com.example.pinart_ma.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.FCMRepository
import com.example.pinart_ma.service.repository.TagRepository

class FCMViewModel(private val fcmRepository:  FCMRepository) : ViewModel() {

    fun suscriberTopic(idUser: String?, token:String?) =  fcmRepository.suscriberTopic(idUser, token);

    fun unsuscriberTopic(idUser: String?, token:String?) =  fcmRepository.unsuscriberTopic(idUser, token);

    fun notifyTopic(idUser: String?, tittle: String?, massage: String?) = fcmRepository.notifyTopic(idUser, tittle, massage);

    fun getTopicsSuscriber(token:String?)  = fcmRepository.getTopicsSuscriber(token);
}