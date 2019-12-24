package com.example.youtubeparcer

import android.annotation.SuppressLint
import android.app.Application
import com.example.youtubeparcer.model.ItemsItem

@SuppressLint("Registered")
class App: Application() {

    private var item: ItemsItem? = null

    fun getItem(){
        item?.snippet?.title
    }
}