package com.poncegl.sigc.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poncegl.sigc.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onLogoutConfirmed() {
        viewModelScope.launch {
            authRepository.logout()

            _uiEvent.send(HomeUiEvent.NavigateToLogin)
        }
    }
}


sealed interface HomeUiEvent {
    data object NavigateToLogin : HomeUiEvent
}