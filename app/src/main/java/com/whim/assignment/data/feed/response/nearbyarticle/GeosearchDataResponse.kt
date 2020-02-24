package com.whim.assignment.data.feed.response.nearbyarticle

import com.google.gson.annotations.SerializedName

data class GeosearchDataResponse(
    @SerializedName("pageid") val pageid : Int,
    @SerializedName("ns") val ns : Int,
    @SerializedName("title") val title : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @SerializedName("dist") val dist : Double,
    @SerializedName("primary") val primary : String
)