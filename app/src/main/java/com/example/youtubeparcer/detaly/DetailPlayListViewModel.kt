package com.example.youtubeparcer.detaly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.model.DetailModelClass
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.ItemsItem
import com.example.youtubeparcer.repository.MainRepository

class DetailPlayListViewModel : ViewModel() {

    fun fetchDetailPlayListData(id: String): LiveData<DetailModelClass> {
        Log.e("----", id)
        return MainRepository.fetchYouTubeDetilPlayListdata(id)

    }
}