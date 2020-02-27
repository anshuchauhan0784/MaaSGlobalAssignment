package com.whim.assignment.domain

import com.whim.assignment.common.Resource
import com.whim.assignment.data.feed.GetRouteRepository
import com.whim.assignment.ui.model.ArticleGeoData
import com.whim.assignment.ui.model.RouteData
import io.reactivex.Observable
import javax.inject.Inject

class GetRouteUseCase @Inject constructor(
    private val  repository: GetRouteRepository,
    private val mapper: GetRouteMapper) {


    fun getRouteDetail(origin : String, destination :String, key : String): Observable<Resource<List<RouteData>>> {
        return  repository
            .getRouteDetail(origin,destination,key)
            .map { resource ->
                Resource(
                    status = resource.status,
                    data =  resource.data?.let { mapper.mapFrom(it) },
                    error = resource.error
                )
            }
    }


}