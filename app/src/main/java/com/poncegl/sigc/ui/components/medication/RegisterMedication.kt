package com.poncegl.sigc.ui.components.medication

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowWidthSizeClass
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.core.util.formatTimeAMPM
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcChipGroup
import com.poncegl.sigc.ui.components.shared.SigcSelector
import com.poncegl.sigc.ui.components.shared.SigcSwitch
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation
import com.poncegl.sigc.ui.feature.patients.domain.model.StockStrategy
import com.poncegl.sigc.ui.feature.patients.presentation.register.MedicationFormState
import com.poncegl.sigc.ui.feature.patients.presentation.register.RegisterPatientEvent
import com.poncegl.sigc.ui.theme.SIGCTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMedication(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val widthSizeClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass
    val useTwoColumns = widthSizeClass != WindowWidthSizeClass.COMPACT

    var showTimePicker by remember { mutableStateOf(false) }
    var showTypeSheet by remember { mutableStateOf(false) }

    if (showTypeSheet) {
        MedicationPresentationSheet(
            selectedPresentation = formState.presentation,
            onPresentationSelected = { type ->
                onEvent(RegisterPatientEvent.MedPresentationChanged(type))
                focusManager.clearFocus()
            },
            onDismiss = { showTypeSheet = false }
        )
    }

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

    // --- LAYOUT PRINCIPAL ---
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val mainScrollState = rememberScrollState()

        if (!useTwoColumns) {
            Column(
                modifier = Modifier
                    .widthIn(max = UI.MAX_WIDTH.dp)
                    .fillMaxSize()
                    .verticalScroll(mainScrollState)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionDefinition(formState, onEvent, focusManager) { showTypeSheet = true }
                HorizontalDivider()
                SectionTreatment(formState, onEvent, focusManager) { showTimePicker = true }
                SectionInventory(formState, onEvent, focusManager)
                SectionAdditional(formState, onEvent, focusManager)
                Spacer(modifier = Modifier.height(16.dp))
                SigcButton(
                    text = "Guardar Medicamento",
                    onClick = { onEvent(RegisterPatientEvent.SaveMedicationToList) },
                    modifier = Modifier.fillMaxWidth(),
                    startIcon = Icons.Default.Check,
                    enabled = formState.name.isNotBlank()
                )
            }
        } else {
            // --- TABLET / FOLDABLE (Scroll Unificado) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(mainScrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // COLUMNA IZQUIERDA
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        SectionDefinition(formState, onEvent, focusManager) { showTypeSheet = true }
                        HorizontalDivider()
                        SectionTreatment(formState, onEvent, focusManager) { showTimePicker = true }
                    }

                    // COLUMNA DERECHA
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        SectionInventory(formState, onEvent, focusManager)
                        SectionAdditional(formState, onEvent, focusManager)
                    }
                }



                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

// ==========================================
// BLOQUE A: DEFINICIÓN
// ==========================================
@Composable
private fun SectionDefinition(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
    focusManager: FocusManager,
    onShowTypeSheet: () -> Unit
) {
    SectionHeader(title = "Definición del producto")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SigcTextField(
            value = formState.name,
            onValueChange = { onEvent(RegisterPatientEvent.MedNameChanged(it)) },
            label = "Nombre comercial",
            placeholder = "Ej: Paracetamol, Dermovate",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        SigcSelector(
            value = formState.presentation.label,
            label = "Presentación / Formato",
            onClick = onShowTypeSheet,
            modifier = Modifier.fillMaxWidth()
        )

        SigcTextField(
            value = formState.concentration,
            onValueChange = { onEvent(RegisterPatientEvent.MedConcentrationChanged(it)) },
            label = "Concentración (Opcional)",
            placeholder = "Ej: 500mg, 1%, 20mg/ml",
            helperText = "Ayuda a distinguir entre variantes del mismo medicamento.",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }
}

// ==========================================
// BLOQUE B: TRATAMIENTO
// ==========================================
@Composable
private fun SectionTreatment(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
    focusManager: FocusManager,
    onShowTimePicker: () -> Unit
) {
    SectionHeader(title = "Tratamiento")

    val isCalculatedStrategy = formState.presentation.strategy == StockStrategy.CALCULATED

    // 1. DOSIS
    AnimatedVisibility(
        visible = isCalculatedStrategy,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SigcTextField(
                modifier = Modifier.weight(1f),
                value = formState.dose,
                onValueChange = { onEvent(RegisterPatientEvent.MedDoseChanged(it)) },
                label = "Cantidad por toma",
                placeholder = "Ej: 1, 0.5, 5",
                keyboardType = KeyboardType.Decimal,
                suffix = { Text(formState.unit) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }
    }

    if (!isCalculatedStrategy) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    MaterialTheme.shapes.small
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Para este formato, indica la aplicación en las instrucciones abajo.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

    // 2. FRECUENCIA
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Frecuencia de toma", style = MaterialTheme.typography.labelLarge)

        DayOfWeekSelector(
            selectedDays = formState.frequencyDays,
            onToggleDay = { index -> onEvent(RegisterPatientEvent.MedToggleFrequencyDay(index)) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Horarios",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onShowTimePicker) {
                Icon(Icons.Filled.Add, null, Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Agregar hora")
            }
        }

        if (formState.frequencyTimes.isNotEmpty()) {
            SigcChipGroup(
                items = formState.frequencyTimes,
                onItemClick = { time -> onEvent(RegisterPatientEvent.MedRemoveFrequencyTime(time)) },
                labelProvider = { formatTimeAMPM(it) },
                leadingIconProvider = { Icons.Default.AccessTime }
            )
        } else {
            Text(
                "Sin horarios definidos (A demanda)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    // 3. DURACIÓN
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SigcTextField(
            modifier = Modifier.weight(1f),
            value = formState.durationDays,
            onValueChange = { onEvent(RegisterPatientEvent.MedDurationChanged(it)) },
            label = "Duración (Días)",
            placeholder = "7",
            keyboardType = KeyboardType.Number,
            enabled = !formState.isIndefinite,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Continuo", style = MaterialTheme.typography.labelSmall)
            SigcSwitch(
                checked = formState.isIndefinite,
                onCheckedChange = { onEvent(RegisterPatientEvent.MedIndefiniteToggled(it)) }
            )
        }
    }
}

// ==========================================
// BLOQUE C: INVENTARIO
// ==========================================
@Composable
private fun SectionInventory(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
    focusManager: FocusManager
) {
    SectionHeader(title = "Inventario inicial")
    val inventoryConfig = formState.presentation.getInventoryConfig()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SigcTextField(
                value = formState.unitsPerPackage,
                onValueChange = { onEvent(RegisterPatientEvent.MedUnitsPerPackageChanged(it)) },
                label = inventoryConfig.contentLabel,
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                suffix = {
                    if (inventoryConfig.defaultUnit.isNotEmpty()) Text(inventoryConfig.defaultUnit)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            SigcTextField(
                value = formState.packageCount,
                onValueChange = { onEvent(RegisterPatientEvent.MedPackageCountChanged(it)) },
                label = inventoryConfig.containerLabel,
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        "Recordatorio de stock bajo",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    val description = formState.stockAlertDescription.ifBlank {
                        "Ingresa datos de dosis e inventario para calcular."
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (formState.stockAlertDescription.isNotBlank())
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SigcSwitch(
                    checked = formState.isStockAlertEnabled,
                    onCheckedChange = { onEvent(RegisterPatientEvent.MedAlertSwitchToggled(it)) },
                    enabled = formState.stockAlertDescription.isNotBlank()
                )
            }
        }
    }
}

// ==========================================
// BLOQUE D: OPCIONALES
// ==========================================
@Composable
private fun SectionAdditional(
    formState: MedicationFormState,
    onEvent: (RegisterPatientEvent) -> Unit,
    focusManager: FocusManager
) {
    SectionHeader(title = "Información adicional")
    SigcTextField(
        value = formState.instructions,
        onValueChange = { onEvent(RegisterPatientEvent.MedInstructionsChanged(it)) },
        label = "Instrucciones adicionales",
        placeholder = "Ej: Aplicar capa fina, Triturar pastilla...",
        singleLine = false,
        maxLines = 3,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )

    SigcTextField(
        value = formState.usageReason,
        onValueChange = { onEvent(RegisterPatientEvent.MedReasonChanged(it)) },
        label = "¿Para qué es? (Motivo)",
        placeholder = "Ej: Para el dolor, Para la infección",
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                if (formState.name.isNotBlank()) onEvent(RegisterPatientEvent.SaveMedicationToList)
                else focusManager.clearFocus()
            }
        )
    )
}

// --- COMPONENTES AUXILIARES ---
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DayOfWeekSelector(
    selectedDays: Set<Int>,
    onToggleDay: (Int) -> Unit
) {
    val days = listOf("L", "M", "M", "J", "V", "S", "D")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, label ->
            val dayIndex = index + 1
            val isSelected = selectedDays.contains(dayIndex)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest)
                    .clickable { onToggleDay(dayIndex) }
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(
                            alpha = 0.5f
                        ),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(
    name = "1. Mobile Light",
    device = "id:pixel_5",
    showBackground = true
)
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
                        concentration = "500 mg",
                        dose = "1",
                        unit = "pzas",
                        frequencyTimes = listOf(
                            LocalTime.of(8, 0),
                            LocalTime.of(16, 0),
                            LocalTime.of(23, 0)
                        ),
                        unitsPerPackage = "12",
                        packageCount = "2",
                        isStockAlertEnabled = true,
                        stockAlertDescription = "Avisar cuando queden 9 pzas (aprox. 3 días)"
                    ),
                    onEvent = {},
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
                        presentation = MedicationPresentation.CREAM,
                        name = "Betametasona",
                        concentration = "0.05%",
                        unitsPerPackage = "40",
                        packageCount = "2",
                        instructions = "Aplicar una capa fina en la zona afectada tras el baño.",
                        isStockAlertEnabled = true,
                        stockAlertDescription = "Avisar cuando quede el último envase"
                    ),
                    onEvent = {},
                )
            }
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
private fun RegisterMedicationsFoldDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                RegisterMedication(
                    formState = MedicationFormState(
                        presentation = MedicationPresentation.SYRUP,
                        name = "Tempra",
                        concentration = "Pediátrico",
                        dose = "5",
                        unit = "mL",
                        frequencyDays = setOf(1, 3, 5),
                        frequencyTimes = listOf(LocalTime.of(10, 0)),
                        unitsPerPackage = "120",
                        packageCount = "1",
                        isIndefinite = true,
                        stockAlertDescription = "Avisar cuando queden 45 mL (aprox. 3 días)"
                    ),
                    onEvent = {},
                )
            }
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
private fun RegisterMedicationsTabletDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                RegisterMedication(
                    formState = MedicationFormState(
                        presentation = MedicationPresentation.SYRUP,
                        name = "Tempra",
                        concentration = "Pediátrico",
                        dose = "5",
                        unit = "mL",
                        frequencyDays = setOf(1, 3, 5),
                        frequencyTimes = listOf(LocalTime.of(10, 0)),
                        unitsPerPackage = "120",
                        packageCount = "1",
                        isIndefinite = true,
                        stockAlertDescription = "Avisar cuando queden 45 mL (aprox. 3 días)"
                    ),
                    onEvent = {},
                )
            }
        }
    }
}
