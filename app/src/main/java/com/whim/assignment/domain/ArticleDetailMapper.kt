package com.whim.assignment.domain

import android.util.Log
import com.whim.assignment.common.Mapper
import com.whim.assignment.data.feed.response.articledetail.ArticleDetailResponse
import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.ArticleGeoData
import javax.inject.Inject

class ArticleDetailMapper @Inject constructor() : Mapper<ArticleDetailResponse , ArticleDetail>{


    override fun mapFrom(type: ArticleDetailResponse): ArticleDetail {
        val detail = type.query?.mapResult?.entries.iterator().next().value

        Log.d("Detail","detail.title ${detail.title} detail.description ${detail.description}   detail.images ${ detail.images}" )
        return ArticleDetail(
            detail.title,
            detail.description,
            detail.images
        )
    }

}