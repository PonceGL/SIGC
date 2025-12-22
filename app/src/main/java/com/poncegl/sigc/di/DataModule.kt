package com.poncegl.sigc.di

import com.poncegl.sigc.data.repository.FirebaseAuthRepository
import com.poncegl.sigc.data.repository.UserPreferencesRepository
import com.poncegl.sigc.data.repository.UserRepositoryImpl
import com.poncegl.sigc.domain.repository.AuthRepository
import com.poncegl.sigc.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): com.poncegl.sigc.domain.repository.UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
