package com.poncegl.sigc.ui.feature.patients.presentation.register

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    // 2. Manejar Efectos de Lado (Navegación tras éxito)
    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateToHome()
        }
    }

//    Mock ----------------

    var checked by remember { mutableStateOf(false) }

    val addMedicationAction = {
        Log.i("RegisterPatientScreen", "Medication Action Mock")
        checked = !checked
    }

    RegisterPatientContent(
        state = state,
        isShowingMedicationForm = checked,
        onAddMedicationAction = { addMedicationAction() },
        onNavigateToHome = onNavigateToHome,
        widthSizeClass = widthSizeClass,
    )
}