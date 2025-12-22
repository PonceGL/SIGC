package com.poncegl.sigc.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun saveOnboardingCompleted()
    suspend fun clearOnboarding()
}