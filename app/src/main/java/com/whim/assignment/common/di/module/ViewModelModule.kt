package com.whim.assignment.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whim.assignment.common.di.ViewModelFactory
import com.whim.assignment.common.di.key.ViewModelKey
import com.whim.assignment.ui.ArticleViewModel
import com.whim.assignment.ui.GetRouteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @IntoMap
    @Binds
    @ViewModelKey(ArticleViewModel::class)
    abstract fun provideArticleViewModel(viewModelNearByArticle: ArticleViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @IntoMap
    @Binds
    @ViewModelKey(GetRouteViewModel::class)
    abstract fun provideGetRouteViewModel(getRouteViewModel: GetRouteViewModel): ViewModel
}