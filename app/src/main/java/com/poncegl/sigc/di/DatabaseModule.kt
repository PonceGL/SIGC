package com.poncegl.sigc.di

import android.content.Context
import androidx.room.Room
import com.poncegl.sigc.data.local.AppDatabase
import com.poncegl.sigc.data.local.dao.CareLogDao
import com.poncegl.sigc.data.local.dao.DoseDao
import com.poncegl.sigc.data.local.dao.MedicationDao
import com.poncegl.sigc.data.local.dao.PatientDao
import com.poncegl.sigc.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sigc_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun providePatientDao(database: AppDatabase): PatientDao = database.patientDao()

    @Provides
    fun provideMedicationDao(database: AppDatabase): MedicationDao = database.medicationDao()

    @Provides
    fun provideDoseDao(database: AppDatabase): DoseDao = database.doseDao()

    @Provides
    fun provideCareLogDao(database: AppDatabase): CareLogDao = database.careLogDao()
}
