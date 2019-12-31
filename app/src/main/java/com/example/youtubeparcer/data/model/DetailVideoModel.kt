package com.example.youtubeparcer.data.model

import com.google.gson.annotations.SerializedName

data class DetailVideoModel( @SerializedName("kind")
                             var kind: String,
                             @SerializedName("pageInfo")
                             var pageInfo: PageInfo,
                             @SerializedName("etag")
                             var etag: String,
                             @SerializedName("items")
                             var items: List<ItemsItem>)