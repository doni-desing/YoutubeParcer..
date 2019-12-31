package com.example.youtubeparcer.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeparcer.R
import com.example.youtubeparcer.data.model.ItemsItem
import com.squareup.picasso.Picasso

/**
 * Created by Karukes Sergey on
 */

class DeatilPlaylistAdapter(val function: (ItemsItem) -> Any) : RecyclerView.Adapter<DetailYouTubeViewHolder>() {


    private var list = mutableListOf<ItemsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailYouTubeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_youtube_playlist, parent, false)

        return DetailYouTubeViewHolder(view, function)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DetailYouTubeViewHolder, position: Int) {
        holder.bind(list[position].copy())
    }

    fun updateData(newList: List<ItemsItem>?) {
        list = newList as MutableList<ItemsItem>
        notifyDataSetChanged()
    }
}

class DetailYouTubeViewHolder(itemView: View, val function: (ItemsItem) -> Any) : RecyclerView.ViewHolder(itemView) {

    private var image: ImageView? = null
    private var title: TextView? = null
    private var description: TextView? = null
    private var video: TextView? = null

    init {
        image = itemView.findViewById(R.id.image)
        title = itemView.findViewById(R.id.title)
        description = itemView.findViewById(R.id.description)
        video = itemView.findViewById(R.id.tv_video)

    }

    fun bind(item: ItemsItem) {
        Picasso
            .get()
            .load(item.snippet.thumbnails.default.url)
            .centerCrop()
            .fit()
            .into(image)
        title?.text = item.snippet.title
        video!!.visibility = View.INVISIBLE
        description?.text = item.contentDetails.itemCount
            itemView.setOnClickListener{
                function(item)
            }.toString()
    }

}
