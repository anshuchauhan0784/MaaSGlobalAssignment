package com.whim.assignment.domain

import com.whim.assignment.common.Mapper
import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import com.whim.assignment.ui.model.ArticleGeoData
import javax.inject.Inject

class ArticleListMapper @Inject constructor() : Mapper<ArticleDataResponse , List<ArticleGeoData>> {
    override fun mapFrom(type: ArticleDataResponse): List<ArticleGeoData> {
        return type.query.geosearch.map { itemResponse ->
            ArticleGeoData(itemResponse.pageid,itemResponse.title,itemResponse.lat, itemResponse.lon)
        }
    }

}