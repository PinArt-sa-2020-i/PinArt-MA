package com.example.pinart_ma.viewModel;

import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.MultimediaRepository
import okhttp3.RequestBody
import java.io.File

class MultimediaViewModel(private val multimediaRepository: MultimediaRepository) : ViewModel(){

    fun getMultimediaByUser(token: String?, idUser: String?) = multimediaRepository.getMultimediaByUser(token, idUser)

    fun getFeedUsers(token: String?, idUser: String?) = multimediaRepository.getFeedUsers(token, idUser)

    fun getFeedTags(token: String?, idUser: String?) = multimediaRepository.getFeedTags(token, idUser)

    fun addMultimedia(
        idUser: String?,
        descripcion: String?,
        idEtiquetas: ArrayList<String?>,
        url_imagen: String?,
        formato: String?,
        tamano: String?,
        idBucket: String?
    ) = multimediaRepository.addMultimedia(
        idUser,
        descripcion,
        idEtiquetas,
        url_imagen,
        formato,
        tamano,
        idBucket
    )

    fun upLoadFileBucket(file: File?) = multimediaRepository.upLoadFileBucket(file)

}
