package com.whim.assignment

import android.app.Application
import com.whim.assignment.common.di.component.AppComponent
import com.whim.assignment.common.di.component.DaggerAppComponent

class BaseApplication : Application(){

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }
    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.factory().create(applicationContext)
    }
}