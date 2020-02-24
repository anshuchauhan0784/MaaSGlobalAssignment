package com.whim.assignment.ui



import com.whim.assignment.common.Status
import com.whim.assignment.ui.model.ArticleGeoData


class NearByArticleViewState(
    val status: Status,
    val error: Throwable? = null,
    val data: List<ArticleGeoData>? = null
) {

    fun getArticleList() = data

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}