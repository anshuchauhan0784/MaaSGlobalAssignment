package com.whim.assignment.data.feed

import com.whim.assignment.data.WikiRestInterface
import com.whim.assignment.util.Constants.Companion.ACTION
import com.whim.assignment.util.Constants.Companion.FORMAT
import com.whim.assignment.util.Constants.Companion.LIMIT
import com.whim.assignment.util.Constants.Companion.LIST_CODE
import com.whim.assignment.util.Constants.Companion.RADIUS
import javax.inject.Inject

class ArticleDataSource @Inject constructor(private val wikiRestInterface: WikiRestInterface){

        fun getNearByArticles(latLng : String) = wikiRestInterface.getNearByArticles(
            ACTION,
            LIST_CODE,
            RADIUS,
            latLng,
            LIMIT,
            FORMAT
        )


        fun getArticleDetail(pageId : Int) = wikiRestInterface.getArticleDetail(
            ACTION,
            "info|description|images",
            pageId,
            FORMAT
        )
}