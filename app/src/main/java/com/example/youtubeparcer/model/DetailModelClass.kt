package com.example.youtubeparcer.model

import com.google.gson.annotations.SerializedName

data class DetailModelClass(
    @SerializedName("kind")
    var kind: String,
    @SerializedName("pageInfo")
    var pageInfo: PageInfo,
    @SerializedName("etag")
    var etag: String,
    @SerializedName("items")
    var items: List<ItemsItem>
)