package com.example.youtubeparcer.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.detaly.DetailPlayListActivity
import com.example.youtubeparcer.internet.InternetActivity
import com.example.youtubeparcer.model.ItemsItem
import com.example.youtubeparcer.model.PlaylistModel
import kotlinx.android.synthetic.main.activity_main.*
import android.R
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.youtubeparcer.internet.InternetCheck
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var inter: InternetCheck? = null
    private var adapter: PlaylistAdapter? = null
    private var viewModel: MainViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(com.example.youtubeparcer.R.layout.activity_main)
        inter = InternetCheck()
        initAdapter()
        isOnline(this)
        fetchPlayList()

    }

    private fun fetchPlayList() {
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

    private fun initAdapter() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = PlaylistAdapter{item: ItemsItem -> clickItem(item)}
        recycler_view.adapter = adapter
    }

    private fun clickItem(item: ItemsItem) {
        val intent =  Intent(this, DetailPlayListActivity::class.java)
        intent.putExtra("id",item.id)
        intent.putExtra("title",item.snippet.title)
        intent.putExtra("channelId",item.snippet.channelId)
        intent.putExtra("eatg",item.etag)
        startActivity(intent)
    }

    fun isOnline(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return if(networkInfo == null ){
            startActivity(Intent(this, InternetActivity::class.java))
        } else{

        }
    }



    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }


}
