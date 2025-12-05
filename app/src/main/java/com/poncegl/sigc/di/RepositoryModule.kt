package com.poncegl.sigc.di

import com.poncegl.sigc.data.repository.SigcRepositoryImpl
import com.poncegl.sigc.domain.repository.SigcRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSigcRepository(
        sigcRepositoryImpl: SigcRepositoryImpl
    ): SigcRepository
}
