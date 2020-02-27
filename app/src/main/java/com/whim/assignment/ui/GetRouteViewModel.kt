package com.whim.assignment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whim.assignment.common.Resource
import com.whim.assignment.common.RxAwareViewModel
import com.whim.assignment.common.ui.plusAssign
import com.whim.assignment.domain.GetRouteUseCase
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.RouteData
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class GetRouteViewModel @Inject constructor(private val getRouteUseCase: GetRouteUseCase) : RxAwareViewModel() {

    private val routeData =  MutableLiveData<GetRouteViewState>()

    fun getRouteLiveData(): LiveData<GetRouteViewState> = routeData


    fun getRouteDetail(origin : String, destination :String, key : String){
        getRouteUseCase
            .getRouteDetail(origin,destination,key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResultArticleDetailReady)
            .also {
                disposable += it
            }
    }




    private fun onResultArticleDetailReady(resource: Resource<List<RouteData>>) {
        routeData.value = GetRouteViewState(
            status = resource.status,
            error = resource.error,
            data = resource.data
        )

    }
}