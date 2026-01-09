package com.poncegl.sigc.di

import com.poncegl.sigc.ui.feature.tracking.data.repository.TrackingRepositoryImpl
import com.poncegl.sigc.ui.feature.tracking.domain.repository.TrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract

class TrackingDataModule {
    @Binds
    @Singleton
    abstract fun bindTrackingRepository(
        impl: TrackingRepositoryImpl
    ): TrackingRepository
}