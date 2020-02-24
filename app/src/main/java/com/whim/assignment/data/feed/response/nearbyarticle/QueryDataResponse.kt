package com.whim.assignment.data.feed.response.nearbyarticle

import com.google.gson.annotations.SerializedName
import com.whim.assignment.data.feed.response.nearbyarticle.GeosearchDataResponse

data class QueryDataResponse(
    @SerializedName("geosearch") val geosearch : List<GeosearchDataResponse>
)