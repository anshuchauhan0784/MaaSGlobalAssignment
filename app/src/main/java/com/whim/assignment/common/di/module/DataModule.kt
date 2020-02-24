package com.whim.assignment.common.di.module

import com.whim.assignment.data.NetworkModule
import dagger.Module


@Module(
    includes = [NetworkModule::class]
)
class DataModule{

}