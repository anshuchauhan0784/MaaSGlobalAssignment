package com.whim.assignment.data.feed

import com.whim.assignment.common.Resource
import com.whim.assignment.common.ui.applyLoading
import com.whim.assignment.data.feed.response.getroute.GetRouteResponse
import com.whim.assignment.data.feed.response.nearbyarticle.ArticleDataResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetRouteRepository @Inject constructor(private val getRouteDataSource: GetRouteDataSource){


    fun getRouteDetail(origin : String, destination :String, key : String) : Observable<Resource<GetRouteResponse>> =
        getRouteDataSource
            .getRouteDetail(origin,destination,key)
            .map {
                Resource.success(it)
            }
            .onErrorReturn { Resource.error(it) }
            .subscribeOn(Schedulers.io())
            .compose(applyLoading())
}