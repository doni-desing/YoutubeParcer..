package com.example.youtubeparcer.detail_video

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.YtVideo
import com.example.youtubeparcer.utils.CallBacks
import com.example.youtubeparcer.utils.PlayerManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.offline.DownloadAction
import com.google.android.exoplayer2.offline.Downloader
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import kotlinx.android.synthetic.main.activity_detail_video.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import java.io.DataOutputStream


@SuppressLint("ByteOrderMark")
class VideoActivity : AppCompatActivity(), CallBacks.playerCallBack {
    private var viewModel: DetailViewModel? = null

    private var videoId: String? = null
    private var playlistId: String? = null
    private var content: String? = null
    private var notificationManager: NotificationManager? = null

    private val ITAG_FOR_AUDIO = 140

    private lateinit var player: Player
    private lateinit var playerManager: PlayerManager

    private var formatsToShowList: MutableList<YtVideo>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        createNotificationChannel(
            "com.ebookfrenzy.notifydemo.news",
            "NotifyDemo News",
            "Example News Channel"
        )

        formatsToShowList = ArrayList()
        playerManager = PlayerManager.getSharedInstance(this)
        player = playerManager.playerView.player
        getData()

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        fetchDetailVideo()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String, name: String,
        description: String
    ) {

        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)

        notificationManager?.createNotificationChannel(channel)
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


    private fun downloadVideo(){
        object : DownloadAction("", 1, null, false, ByteArray(1)){
            override fun createDownloader(downloaderConstructorHelper: DownloaderConstructorHelper?): Downloader {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun writeToStream(output: DataOutputStream?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }
    @SuppressLint("StaticFieldLeak")
    private fun actualLink(link: String) {
        object : YouTubeExtractor(this) {
            public override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                vMeta: VideoMeta
            ) {

                formatsToShowList = java.util.ArrayList<YtVideo>()
                var i = 0
                var itag: Int
                if (ytFiles != null) {
                    while (i < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        val ytFile = ytFiles.get(itag)

                        if (ytFile.format.height == -1 || ytFile.format.height >= 360) {
                            addFormatToList(ytFile)
                        }
                        i++
                    }
                }

                (formatsToShowList as java.util.ArrayList<YtVideo>).sortWith(Comparator { lhs, rhs ->
                    lhs.height - rhs.height
                })

                val yotutubeUrl =
                    (formatsToShowList as java.util.ArrayList<YtVideo>)[(formatsToShowList as ArrayList<YtVideo>).lastIndex]
                playVideo(yotutubeUrl.videoFile!!.url)
            }
        }.extract(link, true, true)
    }


    private fun addFormatToList(ytFile: YtFile) {
        val height = ytFile.format.height
        if (height != -1) {
            for (frVideo in this.formatsToShowList!!) {
                if (frVideo.height == height && (frVideo.videoFile == null || frVideo.videoFile!!.format.fps == ytFile.format.fps)) {
                    return
                }
            }
            if (ytFile.format.height == Configuration.ORIENTATION_UNDEFINED){
                player_view
            }
        }
        val frVideo = YtVideo()
        frVideo.height = height
        if (ytFile.format.isDashContainer) {
            if (height > 0) {
                frVideo.videoFile = ytFile
                frVideo.audioFile = ytFile
            } else {
                frVideo.audioFile = ytFile
            }
        } else {
            frVideo.videoFile = ytFile
        }
        formatsToShowList!!.add(frVideo)
    }

    private fun playVideo(url: String) {
        player_view?.player = player
        PlayerManager.getSharedInstance(this).playStream(url)
        PlayerManager.getSharedInstance(this).setPlayerListener(this)
    }

    override fun onPlayingEnd() {
    }

    override fun onItemClickOnItem(albumId: Int) {

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun clicelable(view: View) { val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null)
        val mBuilder = AlertDialog.Builder(this)

        mBuilder.setTitle("Select video quality")
        mBuilder.setView(mDialogView)
        val AlertDialog = mBuilder.show()

        mDialogView.click.setOnClickListener {
            val channelID = "com.ebookfrenzy.notifydemo.news"

            val notification = Notification.Builder(
                this,
                channelID
            )
                .setContentTitle("Example Notification")
                .setContentText("This is an  example notification.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()

            notificationManager?.notify(0, notification)
            AlertDialog.dismiss()

        }



        AlertDialog.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                player_view.width.and(500)
                player_view.height.and(500)
            }
            }
        }
}










