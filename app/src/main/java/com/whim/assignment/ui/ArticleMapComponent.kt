package com.whim.assignment.ui

import com.whim.assignment.common.di.scope.ActivityScope
import com.whim.assignment.ui.activity.ArticleMapActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent
interface ArticleMapComponent{


    // Factory to create instances of ArticleMapComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ArticleMapComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: ArticleMapActivity)
}
