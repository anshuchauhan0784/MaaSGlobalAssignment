package com.whim.assignment.domain

import com.whim.assignment.common.Resource
import com.whim.assignment.data.feed.ArticleRepository
import com.whim.assignment.data.feed.response.articledetail.ArticleDetailResponse
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.ArticleGeoData
import io.reactivex.Observable
import javax.inject.Inject

class FetchArticleDetailUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val mapper: ArticleDetailMapper
    ){


    fun getArticleDetail(pageId : Int) : Observable<Resource<ArticleDetail>> {
        return  repository
            .getArticleDetail(pageId)
            .map { resource ->
                Resource(
                    status = resource.status,
                    data =  resource.data?.let { mapper.mapFrom(it) },
                    error = resource.error
                )
            }
    }
}