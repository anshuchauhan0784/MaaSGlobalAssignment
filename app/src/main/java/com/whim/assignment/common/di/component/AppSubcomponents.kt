package com.whim.assignment.common.di.component

import com.whim.assignment.ui.ArticleMapComponent
import dagger.Module

@Module(
    subcomponents = [
        ArticleMapComponent::class
    ]
)
class AppSubcomponents