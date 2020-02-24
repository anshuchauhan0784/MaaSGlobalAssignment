package com.whim.assignment.domain

import com.whim.assignment.common.Resource
import com.whim.assignment.data.feed.ArticleRepository
import com.whim.assignment.ui.model.ArticleGeoData
import io.reactivex.Observable
import javax.inject.Inject

class FetchNearByArticleUseCase  @Inject constructor(
    private val repository: ArticleRepository,
    private val mapper: ArticleListMapper
){


    fun getNearByArticles(latLng : String) : Observable<Resource<List<ArticleGeoData>>>{
        return  repository
            .getNearByArticles(latLng)
            .map { resource ->
                Resource(
                    status = resource.status,
                    data =  resource.data?.let { mapper.mapFrom(it) },
                    error = resource.error
                )
            }
    }

}