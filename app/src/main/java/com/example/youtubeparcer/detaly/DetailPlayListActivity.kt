package com.example.youtubeparcer.detaly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.DeatilPlaylistAdapter
import com.example.youtubeparcer.detail_video.VideoActivity
//import com.example.youtubeparcer.detail_video.VideoActivity
import com.example.youtubeparcer.model.DetailModelClass
import com.example.youtubeparcer.model.ItemsItem

import kotlinx.android.synthetic.main.activity_detail_play_list.*


class DetailPlayListActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailPlayListViewModel
    private lateinit var adapter: DeatilPlaylistAdapter
    private var id: String? = null
    private var title: String? = null
    private var appBar: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_play_list)
        viewModel = ViewModelProviders.of(this).get(DetailPlayListViewModel::class.java)
        getIntentData()
        intiAdapter()
        subscribeToViewModel()


    }
    private fun getIntentData(){
        id = intent?.getStringExtra("id")
        title = intent?.getStringExtra("title")
        description = intent?.getStringExtra("etag")
    }

    private fun intiAdapter(){
        recycler_view_in_detail.layoutManager = LinearLayoutManager(this)
        adapter = DeatilPlaylistAdapter { item: ItemsItem -> click(item)}
        recycler_view_in_detail.adapter = adapter
    }

    private fun click(item: ItemsItem) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("content", item.contentDetails.itemCount)
        intent.putExtra("videoId", item.snippet.resourceId.videoId)
        startActivity(intent)
    }

    private fun subscribeToViewModel(){
        Log.e("--", id)
     val data = id?.let { viewModel.fetchDetailPlayListData(it) }
       data?.observe(this, Observer <DetailModelClass>{
           if (data.value != null){
               updataViews(data.value!!)
           }

       })
    }
    private fun updataViews(it: DetailModelClass){
        adapter.updateData(it.items)
    }

}
