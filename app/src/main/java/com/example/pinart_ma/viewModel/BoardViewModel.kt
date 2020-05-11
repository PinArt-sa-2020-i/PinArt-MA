package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.BoardRepository

class BoardViewModel(private val boardRepository: BoardRepository) : ViewModel(){

    fun createBoard(token: String?, idUser: String?, name: String?, descripcion: String?) = boardRepository.createBoard(token, idUser, name, descripcion)

    fun getBoardsUser(token: String?, idUser: String?) = boardRepository.getBoardsUser(token, idUser)
}

