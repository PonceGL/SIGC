package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcCallout
import com.poncegl.sigc.ui.components.shared.SigcDatePicker
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientEvent
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientUiState
import com.poncegl.sigc.ui.theme.SIGCTheme
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@Composable
fun PatientData(
    state: RegisterPatientUiState,
    onEvent: (RegisterPatientEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Fechas límite visuales | Hoy y hace 120 años
    val today = Calendar.getInstance().timeInMillis
    val minDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -120)
    }.timeInMillis

    val isFormValid = state.patientName.isNotBlank() &&
            (state.patientDob != null) &&
            state.diagnosisName.isNotBlank()

    Column(
        modifier = Modifier
            .widthIn(max = UI.MAX_WIDTH.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        SigcCallout(
            title = "Información requerida",
            description = "Estos datos son necesarios para identificar y dar seguimiento al paciente."
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. NOMBRE
            SigcTextField(
                value = state.patientName,
                onValueChange = { onEvent(RegisterPatientEvent.NameChanged(it)) },
                label = "¿Cómo se llama el paciente?",
                isError = state.error != null && state.patientName.isBlank(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            AnimatedContent(
                targetState = state.isDobUnknown,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                        animationSpec = tween(
                            300
                        )
                    )
                },
                label = "DatePickerAnimation"
            ) { unknown ->

                if (!unknown) {
                    // CONVERSIÓN: LocalDate (Domain) -> Long (UI Millis)
                    val dateMillis = state.patientDob?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()
                        ?.toEpochMilli()

                    SigcDatePicker(
                        label = "Fecha de nacimiento",
                        selectedDate = dateMillis,
                        onDateSelected = { millis ->
                            if (millis != null) {
                                // CONVERSIÓN: Long (UI Millis) -> LocalDate (Domain)
                                val localDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                onEvent(RegisterPatientEvent.DobChanged(localDate))
                            }
                        },
                        minDateMillis = minDate,
                        maxDateMillis = today,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                            todayDateBorderColor = MaterialTheme.colorScheme.primary,
                            todayContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // 3. CHECKBOX "NO CONOZCO FECHA"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = state.isDobUnknown,
                    onCheckedChange = { checked ->
                        onEvent(RegisterPatientEvent.DobUnknownChanged(checked))
                    }
                )
                Text(
                    text = "No conozco la fecha exacta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 4. EDAD MANUAL
            SigcTextField(
                value = state.patientAgeInput,
                onValueChange = { input ->
                    if (input.all { char -> char.isDigit() }) {
                        onEvent(RegisterPatientEvent.AgeChanged(input))
                    }
                },
                label = "¿Cuántos años tiene?",
                keyboardType = KeyboardType.Number,
                enabled = state.isDobUnknown,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            // 5. DIAGNÓSTICO
            SigcTextField(
                value = state.diagnosisName,
                onValueChange = { onEvent(RegisterPatientEvent.DiagnosisChanged(it)) },
                label = "¿Cuál es su diagnóstico o estado actual?",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.heightIn(130.dp),
                singleLine = false,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isFormValid) {
                            onEvent(RegisterPatientEvent.NextStep)
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            SigcButton(
                text = "Continuar",
                onClick = {
                    onEvent(RegisterPatientEvent.NextStep)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            )
        }
    }
}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun PatientDataLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            PatientData(
                state = RegisterPatientUiState(
                    currentStep = 1,
                    patientName = "Juan Perez",
                    isDobUnknown = false,
                    patientAgeInput = "35"
                ),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Compact,
            )
        }
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
        Surface {
            PatientData(
                state = RegisterPatientUiState(
                    currentStep = 1,
                    patientName = "Juan Perez",
                    isDobUnknown = true,
                    patientAgeInput = "35"
                ),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Compact,
            )
        }
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
        Surface {
            PatientData(
                state = RegisterPatientUiState(
                    currentStep = 1,
                    patientName = "Juan Perez",
                    isDobUnknown = true,
                    patientAgeInput = "35"
                ),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Expanded,
            )
        }
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
        Surface {
            PatientData(
                state = RegisterPatientUiState(
                    currentStep = 1,
                    patientName = "Juan Perez",
                    isDobUnknown = true,
                    patientAgeInput = "35"
                ),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Expanded,
            )
        }
    }
}
