package com.poncegl.sigc.ui.components.medication

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcSelector
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationUnit
import com.poncegl.sigc.ui.feature.patients.presentation.register.MedicationFormState
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientEvent
import com.poncegl.sigc.ui.theme.SIGCTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMedication(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass,
) {
    val scrollState = rememberScrollState()
    var showTimePicker by remember { mutableStateOf(false) }
    var showTypeSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val inventoryConfig = formState.presentation.getInventoryConfig()

    // --- SHEET DE SELECCIÓN DE TIPO ---
    if (showTypeSheet) {
        MedicationPresentationSheet(
            selectedPresentation = formState.presentation,
            onPresentationSelected = { type ->
                onEvent(RegisterPatientEvent.MedPresentationChanged(type))
            },
            onDismiss = { showTypeSheet = false }
        )
    }

    // --- DIÁLOGO DE HORA ---
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
                        TextButton(onClick = {
                            val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            onEvent(RegisterPatientEvent.MedAddFrequencyTime(time))
                            showTimePicker = false
                        }) { Text("Aceptar") }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .widthIn(max = UI.MAX_WIDTH.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 0. NOMBRE
            SigcTextField(
                value = formState.name,
                onValueChange = { onEvent(RegisterPatientEvent.MedNameChanged(it)) },
                label = "Nombre del medicamento",
                placeholder = "Ejemplo: Paracetamol",
                keyboardType = KeyboardType.Text,
            )

            // 1. SELECTOR DE TIPO (HEADER CONTEXTUAL)
            SigcSelector(
                value = formState.presentation.label,
                label = "Tipo de medicamento",
                onClick = { showTypeSheet = true },
                modifier = Modifier.fillMaxWidth()
            )

            // 2. DOSIS Y UNIDAD
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                SigcTextField(
                    modifier = Modifier.weight(2f),
                    value = formState.dose,
                    onValueChange = { onEvent(RegisterPatientEvent.MedDoseChanged(it)) },
                    label = "Dosis / Concentración",
                    placeholder = "Ej: 500",
                    keyboardType = KeyboardType.Number,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1.2f),
                ) {

                    val medicationUnitArray: Array<MedicationUnit> = MedicationUnit.values()
                    val medicationUnitList: List<MedicationUnit> = medicationUnitArray.toList()
                    SigcTextField(
                        value = "",
                        onValueChange = {},
                        label = formState.unit,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        medicationUnitList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.label) },
                                onClick = {
                                    onEvent(RegisterPatientEvent.MedUnitChanged(option.label))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // 3. FRECUENCIA
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿A qué horas se toma?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    TextButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Filled.Add, null, Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Agregar hora", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    formState.frequencyTimes.forEach { time ->
                        InputChip(
                            selected = true,
                            onClick = { onEvent(RegisterPatientEvent.MedRemoveFrequencyTime(time)) },
                            label = { Text(time.format(DateTimeFormatter.ofPattern("HH:mm"))) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Borrar",
                                    Modifier.size(16.dp)
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        )
                    }
                }
            }

            // 4. DURACIÓN
            SigcTextField(
                value = formState.durationDays,
                onValueChange = { onEvent(RegisterPatientEvent.MedDurationChanged(it)) },
                label = "¿Por cuántos días?",
                placeholder = "7",
                keyboardType = KeyboardType.Number,
            )

            // 5. INVENTARIO CONTEXTUAL (La magia ocurre aquí)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Más sutil
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Inventario inicial",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = "Ayuda a calcular cuándo se terminará el medicamento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    SigcTextField(
                        value = formState.unitsPerPackage,
                        onValueChange = { onEvent(RegisterPatientEvent.MedUnitsPerPackageChanged(it)) },
                        label = inventoryConfig.contentLabel,
                        placeholder = "0",
                        keyboardType = KeyboardType.Number,
                        suffix = { Text(inventoryConfig.defaultUnit) }
                    )

                    SigcTextField(
                        value = formState.packageCount,
                        onValueChange = { onEvent(RegisterPatientEvent.MedPackageCountChanged(it)) },
                        label = inventoryConfig.containerLabel,
                        placeholder = "0",
                        keyboardType = KeyboardType.Number,
                    )
                    val units = formState.unitsPerPackage.toDoubleOrNull() ?: 0.0
                    val packs = formState.packageCount.toDoubleOrNull() ?: 0.0
                    val total = units * packs

                    if (total > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Total en stock: ${total.toInt()} ${inventoryConfig.defaultUnit}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Alerta de stock bajo",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Avisar cuando queden pocas unidades",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = formState.stockAlertThreshold != "0",
                            onCheckedChange = { isChecked ->
                                onEvent(RegisterPatientEvent.MedAlertSwitchToggled(isChecked))
                            },
                        )
                    }
                }
            }

            // 6. OPCIONALES
            SigcTextField(
                value = formState.instructions,
                onValueChange = { onEvent(RegisterPatientEvent.MedInstructionsChanged(it)) },
                label = "Indicación especial (opcional)",
                placeholder = "Ej: Triturar pastilla",
            )

            SigcTextField(
                value = formState.usageReason,
                onValueChange = { onEvent(RegisterPatientEvent.MedReasonChanged(it)) },
                label = "¿Para qué es? (opcional)",
                placeholder = "Ej: Para el dolor",
            )

            Spacer(modifier = Modifier.weight(1f))

            SigcButton(
                text = "Guardar",
                onClick = { onEvent(RegisterPatientEvent.SaveMedicationToList) },
                modifier = Modifier.fillMaxWidth(),
                startIcon = Icons.Default.Check,
                enabled = formState.name.isNotBlank()
            )
        }
    }
}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun RegisterMedicationsLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                RegisterMedication(
                    formState = MedicationFormState(
                        presentation = MedicationPresentation.TABLET,
                        name = "Paracetamol",
                        dose = "10",
                        unit = "unidades",
                    ),
                    onEvent = {},
                    widthSizeClass = WindowWidthSizeClass.Compact
                )
            }
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
private fun RegisterMedicationsDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                RegisterMedication(
                    formState = MedicationFormState(
                        presentation = MedicationPresentation.INJECTION,
                        name = "Paracetamol",
                        dose = "500",
                        unit = "mg",
                    ),
                    onEvent = {},
                    widthSizeClass = WindowWidthSizeClass.Compact
                )
            }
        }
    }
}
