package com.whim.assignment.data

import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface  WikiRestInterface{

    @GET("/w/api.php/")
    fun getNearByArticles(
        @Query("action") action: String,
        @Query("list") list: String,
        @Query("gsradius") gsradius: String,
        @Query("gscoord") gscoord: String,
        @Query("gslimit") gslimit: String,
        @Query("format") format: String
    ) : Observable<ArticleDataResponse>

}