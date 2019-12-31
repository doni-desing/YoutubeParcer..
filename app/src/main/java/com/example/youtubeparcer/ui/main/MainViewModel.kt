package com.example.youtubeparcer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.data.model.PlaylistModel
import com.example.youtubeparcer.repository.MainRepository

class MainViewModel: ViewModel() {

    fun getPlayListData(): LiveData<PlaylistModel>{
        return MainRepository.fetchYouTubePlayListModel()
    }
}