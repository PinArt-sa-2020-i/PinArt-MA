package com.example.pinart_ma.viewModel

import androidx.lifecycle.ViewModel
import com.example.pinart_ma.service.repository.BoardRepository

class BoardViewModel(private val boardRepository: BoardRepository) : ViewModel(){

    fun createBoard(token: String?, idUser: String?, name: String?, descripcion: String?) = boardRepository.createBoard(token, idUser, name, descripcion)

    fun getBoardById(token: String?, idBoard: String?) = boardRepository.getBoardById(token, idBoard)

    fun getBoardsUser(token: String?, idUser: String?) = boardRepository.getBoardsUser(token, idUser)

    fun getBoardsFollowByUser(token: String?, idUser: String?) = boardRepository.getBoardsFollowByUser(token, idUser)

    fun addMultimediaBoard(token: String?, idMultimedia: String?, idBoard: String?) = boardRepository.addMultimediaBoard(token, idMultimedia, idBoard)

    fun removeMultimediaBoard(token: String?, idMultimedia: String?, idBoard: String?) = boardRepository.removeMultimediaBoard(token, idMultimedia, idBoard)

    fun getAllBoardFollow(token: String?) = boardRepository.getAllBoardFollow(token)

    fun addUserFollowBoard(token: String?, idUser: String?, idBoard: String?) = boardRepository.addUserFollowBoard(token, idUser, idBoard)

    fun deleteUserFollowBoard(token: String?, idUserFollowBoard: String?) = boardRepository.deleteUserFollowBoard(token, idUserFollowBoard)
}

