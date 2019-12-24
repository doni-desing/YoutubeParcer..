package com.example.youtubeparcer.detail_video

import android.os.Bundle
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.youtubeparcer.App
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.PlaylistModel


class DetailVideoActivity : AppCompatActivity() {
    private var adapter: PlaylistAdapter? = null
    private var viewModel: DetailViewModel? = null
    private var videoView: VideoView? = null

    private var id: String? = null
    private var videoId: String? = null
    private var title: String? = null
    private var mediaController: MediaController? = null
    private var textView: TextView? = null
    private var app: App? = App()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)

        getData()
        if (mediaController != null) {
            mediaController = MediaController(this)
        }

        textView = findViewById(R.id.tv_detail_video)
        videoView = findViewById(R.id.video_view)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        fetchVideoDetailPlayList()
    }

    private fun fetchVideoDetailPlayList() {
        val data = viewModel?.getPlayListData()
        data?.observe(this, Observer {
            val model: PlaylistModel? = data.value
            when {
                model != null -> {
                    updateAdapterData(model)
                }
            }
        })
    }

    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }

    private fun getData() {
        id = intent?.getStringExtra("id")
        videoId = intent?.getStringExtra("videoId")
        title = intent?.getStringExtra("title")
        textView?.text = app?.getItem().toString()

//        videoView!!.setMediaController(mediaController)
        /*videoView!!.setOnPreparedListener {
            videoView!!.seekTo(position)
            videoView!!.setVideoURI(item?.snippet?.resourceId?.videoId?.toUri())
            if (position == 0)
                videoView!!.start()
        }*/
    }

}
