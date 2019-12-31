package com.example.youtubeparcer.ui.detaly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.data.model.DetailModelClass
import com.example.youtubeparcer.repository.MainRepository

class DetailPlayListViewModel : ViewModel() {

    fun fetchDetailPlayListData(id: String): LiveData<DetailModelClass> {
        Log.e("----", id)
        return MainRepository.fetchYouTubeDetilPlayListdata(id)

    }
}