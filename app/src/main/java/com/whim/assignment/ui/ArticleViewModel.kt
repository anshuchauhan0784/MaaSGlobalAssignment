package com.whim.assignment.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whim.assignment.common.Resource
import com.whim.assignment.common.RxAwareViewModel
import com.whim.assignment.common.ui.plusAssign
import com.whim.assignment.data.feed.response.articledetail.ArticleDetailResponse
import com.whim.assignment.domain.FetchArticleDetailUseCase
import com.whim.assignment.domain.FetchNearByArticleUseCase
import com.whim.assignment.ui.model.ArticleDetail
import com.whim.assignment.ui.model.ArticleGeoData
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private  val  fetchNearByArticleUseCase: FetchNearByArticleUseCase,
    private  val  fetchArticleDetailUseCase: FetchArticleDetailUseCase

) : RxAwareViewModel(){

    private val listNearByArticle = MutableLiveData<NearByArticleViewState>()

    private val articleDetail =  MutableLiveData<ArticleDetailViewState>()

    fun getArticleDetailLiveData(): LiveData<ArticleDetailViewState> = articleDetail

    fun getNearByArticleLiveData(): LiveData<NearByArticleViewState> = listNearByArticle

    fun getNearByArticles(latLng: String){
        fetchNearByArticleUseCase
            .getNearByArticles(latLng)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResultReady)
            .also {
                disposable += it
            }
    }

    private fun onResultReady(resource: Resource<List<ArticleGeoData>>) {

        listNearByArticle.value = NearByArticleViewState(
            status = resource.status,
            error = resource.error,
            data = resource.data
        )

    }


    fun getArticleDetail(pageId : Int){
        fetchArticleDetailUseCase
            .getArticleDetail(pageId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResultArticleDetailReady)
            .also {
                disposable += it
            }
    }




    private fun onResultArticleDetailReady(resource: Resource<ArticleDetail>) {
        articleDetail.value = ArticleDetailViewState(
            status = resource.status,
            error = resource.error,
            data = resource.data
        )

    }


}