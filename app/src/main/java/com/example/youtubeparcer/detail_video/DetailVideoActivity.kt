package com.example.youtubeparcer.detail_video

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.detaly.DetailPlayListActivity
import com.example.youtubeparcer.internet.InternetActivity
import com.example.youtubeparcer.internet.InternetCheck
import com.example.youtubeparcer.model.ItemsItem
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.ui.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class DetailVideoActivity : AppCompatActivity() {
    private var adapter: PlaylistAdapter? = null
    private var viewModel: DetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        fetchVideoDetailPlayList()
    }

    private fun fetchVideoDetailPlayList() {
        val data = viewModel?.getPlayListData()
        data?.observe(this, Observer {
            val model: PlaylistModel? = data.value
            when{
                model != null ->{
                    updateAdapterData(model)
                }
            }
        })
    }

    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }
}
