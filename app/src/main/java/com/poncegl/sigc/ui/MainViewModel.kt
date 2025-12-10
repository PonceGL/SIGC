package com.poncegl.sigc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.data.repository.UserPreferencesRepository
import com.poncegl.sigc.domain.repository.AuthRepository
import com.poncegl.sigc.ui.navigation.HomeDestination
import com.poncegl.sigc.ui.navigation.LoginDestination
import com.poncegl.sigc.ui.navigation.OnboardingDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _startDestination = MutableStateFlow(OnboardingDestination.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        checkStartDestination()
    }

    private fun checkStartDestination() {
        viewModelScope.launch {
            userPreferencesRepository.isOnboardingCompleted.collect { completed ->
                if (!completed) {
                    _startDestination.value = OnboardingDestination.route
                } else {
                    if (authRepository.isUserLoggedIn()) {
                        _startDestination.value = HomeDestination.route
                    } else {
                        _startDestination.value = LoginDestination.route
                    }
                }

                delay(800)
                _isLoading.value = false
            }
        }
    }
}