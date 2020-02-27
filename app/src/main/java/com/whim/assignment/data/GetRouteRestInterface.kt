package com.whim.assignment.data


import com.whim.assignment.data.feed.response.getroute.GetRouteResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRouteRestInterface {

    @GET("/maps/api/directions/json")
    fun getRouteDetail(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String,
        @Query("alternatives") alternatives: Boolean
    ) : Observable<GetRouteResponse>
}