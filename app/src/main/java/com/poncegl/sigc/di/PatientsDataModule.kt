package com.poncegl.sigc.di

import com.poncegl.sigc.ui.feature.patients.data.repository.CarePlanRepositoryImpl
import com.poncegl.sigc.ui.feature.patients.data.repository.PatientRepositoryImpl
import com.poncegl.sigc.ui.feature.patients.domain.repository.CarePlanRepository
import com.poncegl.sigc.ui.feature.patients.domain.repository.PatientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PatientsDataModule {

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        impl: PatientRepositoryImpl
    ): PatientRepository

    @Binds
    @Singleton
    abstract fun bindCarePlanRepository(
        impl: CarePlanRepositoryImpl
    ): CarePlanRepository
}