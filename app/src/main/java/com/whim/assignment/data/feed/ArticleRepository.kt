package com.whim.assignment.data.feed

import com.whim.assignment.common.Resource
import com.whim.assignment.common.ui.applyLoading
import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import io.reactivex.schedulers.Schedulers
import java.util.*
import io.reactivex.Observable
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val  dataSource: ArticleDataSource){

    fun getNearByArticles(latLng : String) : Observable<Resource<ArticleDataResponse>> =
        dataSource
            .getNearByArticles(latLng)
            .map {
                Resource.success(it)
            }
            .onErrorReturn { Resource.error(it) }
            .subscribeOn(Schedulers.io())
            .compose(applyLoading())
}

