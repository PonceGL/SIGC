package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.HeaderAction
import com.poncegl.sigc.ui.components.shared.SigcStepCircle
import com.poncegl.sigc.ui.components.shared.SigcStepper
import com.poncegl.sigc.ui.theme.SIGCTheme

sealed class RegisterPatientStep(val title: String) {
    object One : RegisterPatientStep("1")
    object Two : RegisterPatientStep("2")
    object Three : RegisterPatientStep("3")
    object Four : RegisterPatientStep("4")
}

sealed class HeaderInformation(val title: String, val description: String) {
    object One : HeaderInformation("Datos del paciente", "Información básica")
    object Two : HeaderInformation("Medicamentos", "Tratamiento actual")
    object Three : HeaderInformation("Doctor", "Médico tratante")
    object Four : HeaderInformation("Historial", "Cronograma médico")
}

@Composable
fun RegisterPatientContent(widthSizeClass: WindowWidthSizeClass, onNavigateToHome: () -> Unit) {

    val steps = listOf(
        RegisterPatientStep.One,
        RegisterPatientStep.Two,
        RegisterPatientStep.Three,
        RegisterPatientStep.Four
    )

    val headerInfo = listOf(
        HeaderInformation.One,
        HeaderInformation.Two,
        HeaderInformation.Three,
        HeaderInformation.Four
    )

    var currentStepIndex by remember { mutableIntStateOf(0) }

    val onBackAction = {
        if (currentStepIndex > 0) {
            currentStepIndex--
        } else {
            onNavigateToHome()
        }
    }

    BackHandler(enabled = true) {
        onBackAction()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val currentHeaderInfo = headerInfo[currentStepIndex]

                HeaderAction(
                    title = currentHeaderInfo.title,
                    description = currentHeaderInfo.description,
                    startIconAction = { onBackAction() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SigcStepper(
                    steps = steps,
                    currentStepIndex = currentStepIndex,
                    onStepClick = { index -> currentStepIndex = index },
                    isNavigationEnabled = true,
                    stepIndicator = { step, _, status ->
                        SigcStepCircle(text = step.title, status = status)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = currentStepIndex,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                            animationSpec = tween(
                                300
                            )
                        )
                    },
                    label = "StepContentAnimation"
                ) { targetIndex ->
                    when (steps[targetIndex]) {
                        RegisterPatientStep.One -> StepOne()
                        RegisterPatientStep.Two -> StepTwo()
                        RegisterPatientStep.Three -> StepThree()
                        RegisterPatientStep.Four -> StepFour()
                    }
                }
            }
        }
    }
}

@Composable
fun StepOne() {
    Text(
        "Contenido de la pantalla de StepOne", modifier = Modifier
            .fillMaxSize()
            .background(Color.Green.copy(alpha = 0.1f))
    )
}

@Composable
fun StepTwo() {
    Text(
        "Contenido de la pantalla de StepTwo", modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow.copy(alpha = 0.1f))
    )
}

@Composable
fun StepThree() {
    Text(
        "Contenido de la pantalla de StepThree", modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue.copy(alpha = 0.1f))
    )
}

@Composable
fun StepFour() {
    Text(
        "Contenido de la pantalla de StepFour", modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta.copy(alpha = 0.1f))
    )
}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun PreviewRegisterPatientLight() {
    SIGCTheme(darkTheme = false) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact, onNavigateToHome = {})
    }
}

@Preview(
    name = "2. Mobile Dark",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewRegisterPatientDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact, onNavigateToHome = {})
    }
}
