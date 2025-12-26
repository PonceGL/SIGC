package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcCallout
import com.poncegl.sigc.ui.components.shared.SigcDatePicker
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.theme.SIGCTheme
import java.util.Calendar

@Composable
fun PatientData(widthSizeClass: WindowWidthSizeClass, onContinueAction: () -> Unit) {
    var patientName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf<Long?>(null) }
    var age by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var isAgeUnknown by remember { mutableStateOf(false) }

    // Fechas límite: Hoy y hace 120 años
    val today = Calendar.getInstance().timeInMillis
    val minDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -120)
    }.timeInMillis

    // Función auxiliar para calcular edad
    fun calculateAge(birthDateMillis: Long): String {
        val dob = Calendar.getInstance().apply { timeInMillis = birthDateMillis }
        val todayCal = Calendar.getInstance()
        var ageInt = todayCal.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (todayCal.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            ageInt--
        }
        return ageInt.toString()
    }

    Column(
        modifier = Modifier
            .widthIn(max = UI.MAX_WIDTH.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        SigcCallout(
            title = "Información requerida",
            description = "Estos datos son necesarios para identificar y dar seguimiento al paciente."
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            SigcTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = "¿Cómo se llama el paciente?",
                keyboardType = KeyboardType.Text,
            )

            AnimatedContent(
                targetState = isAgeUnknown,
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
                    SigcDatePicker(
                        label = "Fecha de nacimiento",
                        selectedDate = birthDate,
                        onDateSelected = { date ->
                            birthDate = date
                            if (date != null) {
                                age = calculateAge(date)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = isAgeUnknown,
                    onCheckedChange = { checked ->
                        isAgeUnknown = checked
                        if (checked) {
                            birthDate = null
                            age = ""
                        }
                    }
                )
                Text(
                    text = "No conozco la fecha exacta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            SigcTextField(
                value = age,
                onValueChange = {
                    if (isAgeUnknown) {
                        if (it.all { char -> char.isDigit() }) {
                            age = it
                        }
                    }
                },
                label = "¿Cuántos años tiene?",
                keyboardType = KeyboardType.Number,
                enabled = isAgeUnknown
            )

            SigcTextField(
                value = diagnosis,
                onValueChange = { diagnosis = it },
                label = "¿Cuál es su diagnóstico o estado actual?",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.heightIn(130.dp),
                singleLine = false
            )

            SigcButton(
                text = "Continuar",
                onClick = {
                    Log.i("PatientData", "PatientData: Continuar")
                    onContinueAction()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = patientName.isNotBlank() && age.isNotBlank() && diagnosis.isNotBlank()
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
                widthSizeClass = WindowWidthSizeClass.Compact,
                onContinueAction = {}
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
                widthSizeClass = WindowWidthSizeClass.Compact,
                onContinueAction = {}
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
                widthSizeClass = WindowWidthSizeClass.Expanded,
                onContinueAction = {}
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
                widthSizeClass = WindowWidthSizeClass.Expanded,
                onContinueAction = {}
            )
        }
    }
}
