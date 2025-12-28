package com.poncegl.sigc.ui.feature.registerPatient

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.poncegl.sigc.ui.components.registerPatient.RegisterPatientContent

@Composable
fun RegisterPatientScreen(
    widthSizeClass: WindowWidthSizeClass,
    onNavigateToHome: () -> Unit
) {

    var checked by remember { mutableStateOf(false) }

    val addMedicationAction = {
        Log.i("RegisterPatientScreen", "Medication Action Mock")
        checked = !checked
    }

    RegisterPatientContent(
        widthSizeClass,
        isShowingMedicationForm = checked,
        onAddMedicationAction = { addMedicationAction() },
        onNavigateToHome = onNavigateToHome
    )
}