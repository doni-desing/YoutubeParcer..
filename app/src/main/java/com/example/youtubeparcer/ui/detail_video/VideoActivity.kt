package com.example.youtubeparcer.ui.detail_video

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubeparcer.R
import com.example.youtubeparcer.data.adapter.DownloadDialogAdapter
import com.example.youtubeparcer.data.model.DetailVideoModel
import com.example.youtubeparcer.data.model.YtVideo
import com.example.youtubeparcer.utils.CallBacks
import com.example.youtubeparcer.utils.PlayerManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail_video.*


@SuppressLint("ByteOrderMark")
class VideoActivity : AppCompatActivity(){
    private var viewModel: DetailViewModel? = null

    private var videoId: String? = null
    private var playlistId: String? = null
    private var content: String? = null
    private var notificationManager: NotificationManager? = null

    private var writePermission = false
    private var selectedVideoQuality: String? = null
    private var selectedVideoExt: String? = null
    private var fileVideo: YtVideo? = null

    private lateinit var player: Player
    private lateinit var playerManager: PlayerManager

    private lateinit var dialogDownloadButton: Button
    private lateinit var dialogRecyclerView: RecyclerView

    private lateinit var dialogAdapter: DownloadDialogAdapter

    private lateinit var playerView: PlayerView
    private lateinit var appBarLayout: AppBarLayout

    private val ITAG_FOR_AUDIO = 140

    private var formatsToShowList: MutableList<YtVideo>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)
        initViews()

        setupViews()
        playerManager = PlayerManager.getSharedInstance(this)
        player = playerManager.playerView.player

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager



        formatsToShowList = ArrayList()
        playerManager = PlayerManager.getSharedInstance(this)
        player = playerManager.playerView.player
        getData()

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        fetchDetailVideo()
    }

    private fun initViews() {
        playerView = findViewById(R.id.player_view)
        appBarLayout = findViewById(R.id.app_bar)
    }

    private fun getData() {
        videoId = intent?.getStringExtra("videoId")
        content = intent?.getStringExtra("content")
        playlistId = intent?.getStringExtra("playlistId")
    }

    private fun fetchDetailVideo() {
        val data = videoId?.let { viewModel?.getVideoData(it) }
        data?.observe(this, Observer<DetailVideoModel> {
            val model: DetailVideoModel? = data.value
            when {
                model != null -> {
                    setData(model)
                }
            }
        })
    }

    private fun setData(model: DetailVideoModel) {
        tv_title.text = model.items?.get(0)?.snippet?.title
        content_tv.text = model.items.get(0).contentDetails.itemCount
        tv_description.text = model.items?.get(0)?.snippet?.description
        val link = model.items?.get(0)?.id.toString()
        actualLink(link)

    }


    @SuppressLint("StaticFieldLeak")
    private fun actualLink(link : String) {
        object : YouTubeExtractor(this) {
            public override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta) {

                formatsToShowList = mutableListOf()
                var i = 0
                var itag: Int
                if (ytFiles != null) {
                    while (i < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        val ytFile = ytFiles.get(itag)

                        if (ytFile.format.height == -1 || ytFile.format.height >= 360) {
                            addFormatToList(ytFile, ytFiles)
                        }
                        i++
                    }
                }
                (formatsToShowList)?.sortWith(Comparator {
                        lhs, rhs -> lhs!!.height - rhs!!.height
                })

                val yotutubeUrl: YtVideo? = formatsToShowList?.get(formatsToShowList!!.lastIndex)
                if (yotutubeUrl?.videoFile?.url != null) {
                    playVideo(yotutubeUrl.videoFile?.url!!)
                }
            }
        }.extract(link, true, true)
    }


    private fun addFormatToList(ytFile: YtFile, ytFiles: SparseArray<YtFile>) {
        val height = ytFile.format.height
        if (height != -1) {
            for (frVideo in this.formatsToShowList!!) {
                if (frVideo.height == height && (frVideo.videoFile == null || frVideo.videoFile!!.format.fps == ytFile.format.fps)) {
                    return
                }
            }
        }
        val frVideo = YtVideo()
        frVideo.height = height
        if (ytFile.format.isDashContainer) {
            if (height > 0) {
                frVideo.videoFile = ytFile
                frVideo.audioFile = ytFiles.get(ITAG_FOR_AUDIO)
            } else {
                frVideo.audioFile = ytFile
            }
        } else {
            frVideo.videoFile = ytFile
        }
        formatsToShowList!!.add(frVideo)
    }

    private fun playVideo(url: String) {
        val request: DownloadManager.Request =
            DownloadManager.Request(Uri.parse(url))
                .setDescription("Downloading") // Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
                .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)

        val downloadManager: DownloadManager =
            applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        player_view?.player = player
        DownloadManager.Request(Uri.parse(url))
        PlayerManager.getSharedInstance(this).playStream(url)
        PlayerManager.getSharedInstance(this).isPlayerPlaying
    }


    @SuppressLint("InflateParams")
    private fun showDownloadDialog() {
        val builder = AlertDialog.Builder(this, R.style.DownloadDialog)

        val view = layoutInflater.inflate(R.layout.alert_dwonload_dialg, null)
        builder.setView(view)
        dialogDownloadButton = view.findViewById(R.id.btn_alert_download)
        dialogRecyclerView = view.findViewById(R.id.alert_recycler_view)

        initDialogAdapter()
        dialogAdapter.updateData(formatsToShowList)
        val alert = builder.create()
        alert.show()
        dialogDownloadButton.setOnClickListener {
            alert.dismiss()
        }
    }

    private fun setupViews() {
        btn_download.setOnClickListener {
            checkRequestPermission()
            showDownloadDialog()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            playlist.visibility = View.INVISIBLE
            appBarLayout.visibility = View.INVISIBLE
        } else {
            playerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            playlist.visibility = View.VISIBLE
            appBarLayout.visibility = View.VISIBLE
        }
    }

    private fun initDialogAdapter() {
        dialogAdapter = DownloadDialogAdapter { item: YtVideo -> downloadClickItem(item) }
        dialogRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        dialogRecyclerView.adapter = dialogAdapter
    }

    private fun downloadClickItem(item: YtVideo) {
        selectedVideoQuality = item.videoFile?.url
        selectedVideoExt = item.videoFile?.format?.ext
        fileVideo = item

    }

    private fun checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1024)
        } else {
            writePermission = true
        }
    }
}