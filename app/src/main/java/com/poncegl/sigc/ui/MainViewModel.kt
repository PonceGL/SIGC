package com.poncegl.sigc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.poncegl.sigc.data.repository.UserPreferencesRepository
import com.poncegl.sigc.ui.navigation.LoginDestination
import com.poncegl.sigc.ui.navigation.OnboardingDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _startDestination = MutableStateFlow(OnboardingDestination.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        checkStartDestination()
    }

    private fun checkStartDestination() {
        viewModelScope.launch {
            repository.isOnboardingCompleted.collect { completed ->
                if (completed) {
                    _startDestination.value = LoginDestination.route
                } else {
                    _startDestination.value = OnboardingDestination.route
                }

                delay(800)

                _isLoading.value = false
            }
        }
    }

    companion object {
        fun provideFactory(repository: UserPreferencesRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(repository)
                }
            }
    }
}