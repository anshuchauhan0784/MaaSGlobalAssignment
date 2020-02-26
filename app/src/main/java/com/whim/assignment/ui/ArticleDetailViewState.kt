package com.whim.assignment.ui

import com.whim.assignment.common.Status
import com.whim.assignment.ui.model.ArticleDetail


class ArticleDetailViewState(
    val status: Status,
    val error: Throwable? = null,
    val data: ArticleDetail? = null
) {

    fun getArticleDetail() = data

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}