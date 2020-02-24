package com.whim.assignment.data.feed

import com.whim.assignment.data.WikiRestInterface
import javax.inject.Inject

class ArticleDataSource @Inject constructor(private val wikiRestInterface: WikiRestInterface){

        fun getNearByArticles(latLng : String) = wikiRestInterface.getNearByArticles(
            "query",
            "geosearch",
            "10000",
            latLng,
            "50",
            "json"
        )
}