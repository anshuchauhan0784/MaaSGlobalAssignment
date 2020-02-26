package com.whim.assignment.domain

import android.util.Log
import com.whim.assignment.common.Mapper
import com.whim.assignment.data.feed.response.articledetail.ArticleDetailResponse
import com.whim.assignment.data.feed.response.articledetail.Image
import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.ArticleGeoData
import com.whim.assignment.util.Constants.Companion.IMAGE_BASE_URL
import com.whim.assignment.util.Constants.Companion.IMAGE_WIDTH
import javax.inject.Inject

class ArticleDetailMapper @Inject constructor() : Mapper<ArticleDetailResponse , ArticleDetail>{


    override fun mapFrom(type: ArticleDetailResponse): ArticleDetail {
        val detail = type.query?.mapResult?.entries.iterator().next().value
        val listImageUrl =  createUrlFromImageName(detail.images)
        return ArticleDetail(
            detail.title,
            detail.description,
            listImageUrl
        )
    }


    /**
     * As we are getting the image name only in detail request so this method will convert image name to working url
     * to show image
     */
    private fun createUrlFromImageName(imageList : List<Image>?) : List<String> {

        val imageUrlList =  mutableListOf<String>()

        imageList?.let {
            for (image in it){
                val imageName = image.title.substringAfter("File:")

                val finalUrl = "${IMAGE_BASE_URL}${imageName}?width=$IMAGE_WIDTH"

                imageUrlList.add(finalUrl)
            }
        }

        return imageUrlList
    }

}