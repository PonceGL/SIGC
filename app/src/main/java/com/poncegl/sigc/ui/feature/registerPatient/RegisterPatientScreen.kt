package com.poncegl.sigc.ui.feature.registerPatient

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.poncegl.sigc.ui.components.registerPatient.RegisterPatientContent

@Composable
fun RegisterPatientScreen(
    widthSizeClass: WindowWidthSizeClass,
    onNavigateToHome: () -> Unit
) {

    RegisterPatientContent(widthSizeClass, onNavigateToHome)
}