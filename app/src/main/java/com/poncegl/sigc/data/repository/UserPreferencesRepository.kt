package com.poncegl.sigc.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_preferences"
private const val KEY_ONBOARDING_COMPLETED = "is_onboarding_completed"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class UserPreferencesRepository(private val context: Context) {
    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey(KEY_ONBOARDING_COMPLETED)
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
        }

    suspend fun saveOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun clearOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] = false
        }
    }
}