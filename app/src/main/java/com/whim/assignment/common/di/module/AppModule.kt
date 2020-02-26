package com.whim.assignment.common.di.module

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.whim.assignment.BaseApplication
import com.whim.assignment.R
import com.whim.assignment.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module

class AppModule {

   @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
            .transform(RoundedCorners(Constants.RADIUS_GLIDE))
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Context, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }
}