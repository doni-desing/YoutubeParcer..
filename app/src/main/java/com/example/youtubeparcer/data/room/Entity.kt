package com.example.youtubeparcer.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.youtubeparcer.data.model.ItemsItem
import com.example.youtubeparcer.data.model.PageInfo
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Video_saved")
class Entity(@ColumnInfo(name = "kind")
             var kind: String,
             @ColumnInfo(name = "pageInfo")
             var pageInfo: PageInfo,
             @ColumnInfo(name = "etag")
             var etag: String,
             @ColumnInfo(name = "items")
             var items: List<ItemsItem>)

