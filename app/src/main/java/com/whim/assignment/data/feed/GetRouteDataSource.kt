package com.whim.assignment.data.feed

import com.whim.assignment.data.GetRouteRestInterface

import javax.inject.Inject

class GetRouteDataSource @Inject constructor(private val getRouteRestInterface: GetRouteRestInterface) {

    fun getRouteDetail(origin : String, destination :String, key : String) = getRouteRestInterface.getRouteDetail(
        origin,
        destination,
        key
    )
}