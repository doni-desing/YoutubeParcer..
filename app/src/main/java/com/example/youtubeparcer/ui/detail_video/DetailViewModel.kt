package com.example.youtubeparcer.ui.detail_video

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.data.model.DetailVideoModel
import com.example.youtubeparcer.repository.MainRepository

class DetailViewModel: ViewModel() {
    fun getVideoData(id: String) : LiveData<DetailVideoModel>? {
        return MainRepository.fetchVideoData(id)
    }
}