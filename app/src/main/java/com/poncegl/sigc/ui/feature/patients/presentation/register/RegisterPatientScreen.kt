package com.poncegl.sigc.ui.feature.patients.presentation.register

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poncegl.sigc.ui.components.registerPatient.RegisterPatientContent

@Composable
fun RegisterPatientScreen(
    viewModel: RegisterPatientViewModel = hiltViewModel(),
    widthSizeClass: WindowWidthSizeClass,
    onNavigateToHome: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateToHome()
        }
    }

    RegisterPatientContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToHome = onNavigateToHome,
        widthSizeClass = widthSizeClass,
    )
}