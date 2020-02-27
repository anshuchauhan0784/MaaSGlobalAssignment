package com.whim.assignment.domain

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.whim.assignment.common.Mapper
import com.whim.assignment.data.feed.response.articledetail.ArticleDetailResponse
import com.whim.assignment.data.feed.response.getroute.GetRouteResponse
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.RouteData
import javax.inject.Inject

class GetRouteMapper  @Inject constructor() : Mapper<GetRouteResponse,  List<RouteData>> {

    override fun mapFrom(type: GetRouteResponse): List<RouteData> {

        var routeList  = mutableListOf<RouteData>()

        for (route in type.routes){
            var latLngList  = mutableListOf<LatLng>()
            var routeData = RouteData()
            for (leg in route.legs){
                routeData.distance =  leg.distance.text
                routeData.duration =  leg.duration.text
                for (steps in leg.steps){
                    var polyLineList = PolyUtil.decode(steps.polyline.points)
                    for(latLng in polyLineList){
                        latLngList.add(latLng)
                    }
                }
            }
            routeData.routes = latLngList
            routeList.add(routeData)
        }

        return  routeList
    }

}