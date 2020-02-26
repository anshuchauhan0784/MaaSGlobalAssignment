package com.whim.assignment.data.feed.response.articledetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticleDetailResponse(
    @SerializedName("batchcomplete")
    val batchcomplete: String,
    @SerializedName("query")
    val query: Query
)

data class Query(
    @SerializedName("pages")
    @Expose
    val mapResult : Map<String , Detail>
)


data class Detail(
    val contentmodel: String,
    val description: String,
    val descriptionsource: String,
    val images: List<Image>,
    val lastrevid: Int,
    val length: Int,
    val ns: Int,
    val pageid: Int,
    val pagelanguage: String,
    val pagelanguagedir: String,
    val pagelanguagehtmlcode: String,
    val title: String,
    val touched: String
)

data class Image(
    val ns: Int,
    val title: String
)