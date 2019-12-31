package com.example.youtubeparcer.ui.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.data.adapter.PlaylistAdapter
import com.example.youtubeparcer.ui.detaly.DetailPlayListActivity
import com.example.youtubeparcer.ui.internet.InternetActivity
import com.example.youtubeparcer.data.model.ItemsItem
import com.example.youtubeparcer.data.model.PlaylistModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var adapter: PlaylistAdapter? = null
    private var viewModel: MainViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(com.example.youtubeparcer.R.layout.activity_main)
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
            Toast.makeText(this, "You don't have internet", Toast.LENGTH_LONG).show()

        } else{
            Toast.makeText(this, "You have internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }


}
