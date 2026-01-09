package com.poncegl.sigc.di

import com.poncegl.sigc.core.util.MockNetworkMonitor
import com.poncegl.sigc.core.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(): NetworkMonitor {
        return MockNetworkMonitor() // TODO: Reemplazar por la real cuando la tengas
    }
}