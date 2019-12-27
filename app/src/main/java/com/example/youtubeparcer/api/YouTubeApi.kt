package com.example.youtubeparcer.api

import com.example.youtubeparcer.model.DetailModelClass
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.PlaylistModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    @GET("youtube/v3/playlists")
    fun getPlayList(
        @Query("part") part: String,
        @Query("key") apiKey: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResult: String
    ): Call<PlaylistModel>

    @GET("youtube/v3/playlistItems")
    fun getDetailPlayList(
        @Query("part") part: String,
        @Query("key") apiKey: String,
        @Query("playlistId") playListId: String,
        @Query("maxResults") maxResult: String
    ): Call<DetailModelClass>

    @GET("youtube/v3/videos")
    fun getDetailVideo(
        @Query("key") apiKey: String,
        @Query("part") part: String,
        @Query("id") id: String?
    ): Call<DetailVideoModel>
}