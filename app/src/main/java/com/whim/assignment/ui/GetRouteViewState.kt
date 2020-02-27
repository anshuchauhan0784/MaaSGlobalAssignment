package com.whim.assignment.ui

import com.whim.assignment.common.Status
import com.whim.assignment.ui.model.RouteData

class GetRouteViewState(
    val status: Status,
    val error: Throwable? = null,
    val data: List<RouteData>?
) {

    fun getRouteData() = data

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}