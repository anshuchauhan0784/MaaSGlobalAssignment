package com.whim.assignment.data.feed.response.nearbyarticle

import com.google.gson.annotations.SerializedName

data class ArticleDataResponse(
    @SerializedName("batchcomplete") val batchcomplete : String,
    @SerializedName("query") val query : QueryDataResponse
)