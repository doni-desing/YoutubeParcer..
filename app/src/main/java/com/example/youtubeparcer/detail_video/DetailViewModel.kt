package com.example.youtubeparcer.detail_video

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.repository.MainRepository

class DetailViewModel: ViewModel() {
    fun getPlayListData(): LiveData<PlaylistModel> {
        return MainRepository.fetchYouTubePlayListModel()
    }
}