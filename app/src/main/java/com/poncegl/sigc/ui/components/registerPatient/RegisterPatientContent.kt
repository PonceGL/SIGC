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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.components.shared.HeaderAction
import com.poncegl.sigc.ui.components.shared.HeaderIcon
import com.poncegl.sigc.ui.components.shared.SigcStepCircle
import com.poncegl.sigc.ui.components.shared.SigcStepper
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientEvent
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientUiState
import com.poncegl.sigc.ui.theme.SIGCTheme

sealed class RegisterPatientStep(val title: String) {
    object One : RegisterPatientStep("1")
    object Two : RegisterPatientStep("2")
    object Three : RegisterPatientStep("3")
    object Four : RegisterPatientStep("4")
}

sealed class HeaderInformation(val title: String, val description: String, val icon: HeaderIcon) {
    object One : HeaderInformation(
        title = "Datos del paciente",
        description = "Información básica",
        icon = HeaderIcon.Drawable(R.drawable.person)
    )

    object Two : HeaderInformation(
        title = "Medicamentos",
        description = "Tratamiento actual",
        icon = HeaderIcon.Drawable(R.drawable.medicine_capsule),
    )

    object Three : HeaderInformation(
        title = "Doctor",
        description = "Médico tratante",
        icon = HeaderIcon.Drawable(R.drawable.stethoscope),
    )

    object Four : HeaderInformation(
        title = "Historial",
        description = "Cronograma médico",
        icon = HeaderIcon.Drawable(R.drawable.history),
    )
}

@Composable
fun RegisterPatientContent(
    state: RegisterPatientUiState,
    onEvent: (RegisterPatientEvent) -> Unit,
    onNavigateToHome: () -> Unit,
    widthSizeClass: WindowWidthSizeClass,
) {

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

    val currentStepIndex = state.currentStep

    val onBackAction = {
        if (state.currentStep > 1) {
            onEvent(RegisterPatientEvent.PreviousStep)
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
                .padding(vertical = 10.dp)
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
                val safeHeaderIndex = currentStepIndex.coerceIn(0, headerInfo.lastIndex)
                val currentHeaderInfo = headerInfo[safeHeaderIndex]

                HeaderAction(
                    title = currentHeaderInfo.title,
                    description = currentHeaderInfo.description,
                    startIconAction = { onBackAction() },
                    endIcon = currentHeaderInfo.icon,
                )

                Spacer(modifier = Modifier.height(10.dp))

                SigcStepper(
                    steps = steps,
                    currentStepIndex = safeHeaderIndex,
                    onStepClick = { },
                    isNavigationEnabled = false,
                    stepIndicator = { step, _, status ->
                        SigcStepCircle(text = step.title, status = status)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = safeHeaderIndex,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                            animationSpec = tween(300)
                        )
                    },
                    label = "StepContentAnimation"
                ) { targetIndex ->
                    when (steps[targetIndex]) {

                        RegisterPatientStep.One -> PatientData(
                            state = state,
                            onEvent = onEvent,
                            widthSizeClass = widthSizeClass
                        )

                        RegisterPatientStep.Two -> MedicationsData(
                            widthSizeClass = WindowWidthSizeClass.Compact,
                            isShowingMedicationForm = state.isAddingMedication,
                            onAddMedicationAction = {
                                onEvent(RegisterPatientEvent.StartAddingMedication)
                            },
                            onBackAction = {
                                onBackAction()
                            },
                            onContinueAction = {
                                onEvent(RegisterPatientEvent.NextStep)
                            }
                        )

                        RegisterPatientStep.Three -> StepThree()
                        RegisterPatientStep.Four -> StepFour()
                    }
                }
            }
        }
    }
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
private fun PatientDataLight() {
    SIGCTheme(darkTheme = false) {
        RegisterPatientContent(
            state = RegisterPatientUiState(
                currentStep = 0
            ),
            onEvent = {},
            onNavigateToHome = {},
            widthSizeClass = WindowWidthSizeClass.Compact,
        )
    }
}

@Preview(
    name = "2. Mobile Dark",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PatientDataDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(
            state = RegisterPatientUiState(
                currentStep = 0
            ),
            onEvent = {},
            onNavigateToHome = {},
            widthSizeClass = WindowWidthSizeClass.Compact,
        )
    }
}

@Preview(
    name = "3. Foldable Dark",
    device = "id:pixel_fold",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PatientDataFoldDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(
            state = RegisterPatientUiState(
                currentStep = 0
            ),
            onEvent = {},
            onNavigateToHome = {},
            widthSizeClass = WindowWidthSizeClass.Compact,
        )
    }
}

@Preview(
    name = "4. Tablet Dark",
    device = "id:pixel_tablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PatientDataTabletDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(
            state = RegisterPatientUiState(
                currentStep = 0
            ),
            onEvent = {},
            onNavigateToHome = {},
            widthSizeClass = WindowWidthSizeClass.Compact,
        )
    }
}
