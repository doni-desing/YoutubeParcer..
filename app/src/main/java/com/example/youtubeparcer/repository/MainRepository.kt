package com.example.youtubeparcer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.youtubeparcer.data.api.RetrofitClient
import com.example.youtubeparcer.data.api.YouTubeApi
import com.example.youtubeparcer.data.model.DetailModelClass
import com.example.youtubeparcer.data.model.DetailVideoModel
import com.example.youtubeparcer.data.model.PlaylistModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository {
    companion object {
        val channel = "UC_IfiZu3VkesO3L58L9WPhA"
        val apiKey = "AIzaSyCWK-EoCHecYMMFAvl-DI5iegR9s1WW20Y"
        val part = "snippet,contentDetails"
        val maxResult = "50"

        private lateinit var apiServise: YouTubeApi

        fun fetchYouTubePlayListModel(): LiveData<PlaylistModel> {
            apiServise = RetrofitClient.create()
            val data = MutableLiveData<PlaylistModel>()
            apiServise.getPlayList(part, apiKey, channel, maxResult)
                .enqueue(object : Callback<PlaylistModel> {

                    override fun onFailure(call: Call<PlaylistModel>, t: Throwable) {
                        data.value = null
                    }

                    override fun onResponse(
                        call: Call<PlaylistModel>,
                        response: Response<PlaylistModel>
                    ) {
                        data.value = response.body()
                    }

                })
            return data
        }

        fun fetchVideoData(videoId: String): LiveData<DetailVideoModel>? {
            val apiService = RetrofitClient.create()
            val data = MutableLiveData<DetailVideoModel>()
            apiService.getDetailVideo(apiKey, part, videoId).enqueue(object : Callback<DetailVideoModel> {
                override fun onFailure(call: Call<DetailVideoModel>, t: Throwable) {
                    data.value = null
                }

                override fun onResponse(
                    call: Call<DetailVideoModel>,
                    response: Response<DetailVideoModel>
                ) {
                    data.value = response.body()
                }

            })

            return data
        }

        fun fetchYouTubeDetilPlayListdata(playListId: String): LiveData<DetailModelClass> {
            apiServise = RetrofitClient.create()
            val data = MutableLiveData<DetailModelClass>()
            apiServise.getDetailPlayList(part, apiKey, playListId, maxResult)
                .enqueue(object : Callback<DetailModelClass> {
                    override fun onFailure(call: Call<DetailModelClass>, t: Throwable) {
                        Log.v("responce_faile", t.message)
                        data.value = null
                        Log.d("tag", data.value.toString())

                    }

                    override fun onResponse(
                        call: Call<DetailModelClass>,
                        response: Response<DetailModelClass>
                    ) {
                        data.value = response.body()
                        Log.e("-----", "$data ")
                    }

                })
            return data
        }



    }
}