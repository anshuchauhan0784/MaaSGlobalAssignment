package com.whim.assignment.common.di.component

import android.content.Context
import com.whim.assignment.common.di.module.DataModule
import com.whim.assignment.common.di.module.ViewModelModule
import com.whim.assignment.ui.ArticleMapComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton

//Dagger component that adds info from the different modules to the graph
@Component(modules = [
    AppSubcomponents::class,
    DataModule::class,
    ViewModelModule::class
]
)
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun articleMapComponent(): ArticleMapComponent.Factory

}