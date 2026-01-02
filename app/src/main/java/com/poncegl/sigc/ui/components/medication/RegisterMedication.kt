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
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    widthSizeClass: WindowWidthSizeClass,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var showTimePicker by remember { mutableStateOf(false) }
    var showTypeSheet by remember { mutableStateOf(false) }

    // Configuración contextual (Labels y Unidad)
    val inventoryConfig = formState.presentation.getInventoryConfig()
    val isCalculatedStrategy = formState.presentation.strategy == StockStrategy.CALCULATED

    // --- SHEET DE SELECCIÓN DE TIPO ---
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
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp), // Padding extra para scroll
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp) // Más espacio entre bloques
        ) {

            // ==========================================
            // BLOQUE A: DEFINICIÓN (¿Qué es?)
            // ==========================================
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

                // SELECTOR DE TIPO
                SigcSelector(
                    value = formState.presentation.label,
                    label = "Presentación / Formato",
                    onClick = { showTypeSheet = true },
                    modifier = Modifier.fillMaxWidth()
                )

                // CONCENTRACIÓN (Nuevo)
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

            HorizontalDivider()

            // ==========================================
            // BLOQUE B: TRATAMIENTO (¿Cómo se usa?)
            // ==========================================
            SectionHeader(title = "Tratamiento")

            // 1. DOSIS (Solo visible para Calculables como Pastillas/Jarabes)
            AnimatedVisibility(
                visible = isCalculatedStrategy,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top, // Alineación Top por si hay error
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

                    // Nota: Podríamos agregar un Dropdown de unidad aquí si quisiéramos cambiar mg/ml
                }
            }

            if (!isCalculatedStrategy) {
                // Mensaje informativo para Cremas/Otros
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

            // 2. FRECUENCIA (Días y Horas)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Frecuencia de toma", style = MaterialTheme.typography.labelLarge)

                // Selector de Días (Nuevo Componente Inline)
                DayOfWeekSelector(
                    selectedDays = formState.frequencyDays,
                    onToggleDay = { index ->
                        onEvent(
                            RegisterPatientEvent.MedToggleFrequencyDay(
                                index
                            )
                        )
                    }
                )

                // Selector de Horas
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
                    TextButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Filled.Add, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Agregar hora")
                    }
                }

                if (formState.frequencyTimes.isNotEmpty()) {
                    SigcChipGroup(
                        items = formState.frequencyTimes,
                        onItemClick = { time ->
                            onEvent(
                                RegisterPatientEvent.MedRemoveFrequencyTime(
                                    time
                                )
                            )
                        },
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
                    modifier = Modifier.padding(top = 8.dp) // Ajuste visual
                ) {
                    Text("Continuo", style = MaterialTheme.typography.labelSmall)
                    SigcSwitch(
                        checked = formState.isIndefinite,
                        onCheckedChange = { onEvent(RegisterPatientEvent.MedIndefiniteToggled(it)) }
                    )
                }
            }

            HorizontalDivider()

            // ==========================================
            // BLOQUE C: INVENTARIO (¿Qué tienes?)
            // ==========================================
            SectionHeader(title = "Inventario inicial")

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
                    // Inputs de Cantidad
                    // Context-Aware: Las etiquetas cambian según inventoryConfig (definido en ViewModel/Model)
                    SigcTextField(
                        value = formState.unitsPerPackage,
                        onValueChange = { onEvent(RegisterPatientEvent.MedUnitsPerPackageChanged(it)) },
                        label = inventoryConfig.contentLabel, // Ej: "Piezas por caja" o "Tamaño envase"
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
                        label = inventoryConfig.containerLabel, // Ej: "Cajas" o "Envases"
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

                    // SMART ALERT SECTION
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

                            // AQUÍ ESTÁ EL VALOR: Mostramos la descripción calculada por el ViewModel
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
                            onCheckedChange = {
                                onEvent(
                                    RegisterPatientEvent.MedAlertSwitchToggled(
                                        it
                                    )
                                )
                            },
                            // Deshabilitar si no hay descripción válida calculada (opcional)
                            enabled = formState.stockAlertDescription.isNotBlank()
                        )
                    }
                }
            }

            // ==========================================
            // BLOQUE D: OPCIONALES
            // ==========================================
            SigcTextField(
                value = formState.instructions,
                onValueChange = { onEvent(RegisterPatientEvent.MedInstructionsChanged(it)) },
                label = "Instrucciones adicionales",
                placeholder = "Ej: Aplicar capa fina, Triturar pastilla...",
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            SigcTextField(
                value = formState.usageReason,
                onValueChange = { onEvent(RegisterPatientEvent.MedReasonChanged(it)) },
                label = "¿Para qué es? (Motivo)",
                placeholder = "Ej: Para el dolor, Para la infección",
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (formState.name.isNotBlank()) {
                            onEvent(RegisterPatientEvent.SaveMedicationToList)
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            SigcButton(
                text = "Guardar Medicamento",
                onClick = { onEvent(RegisterPatientEvent.SaveMedicationToList) },
                modifier = Modifier.fillMaxWidth(),
                startIcon = Icons.Default.Check,
                enabled = formState.name.isNotBlank()
            )
        }
    }
}

// --- COMPONENTES AUXILIARES (Locales para mantener atomicidad visual) ---

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
            val dayIndex = index + 1 // 1..7
            val isSelected = selectedDays.contains(dayIndex)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHighest
                    )
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
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
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
                        concentration = "500 mg", // Nuevo campo visible
                        dose = "1",
                        unit = "pzas",
                        // Simulación: Toma 1 cada 8 horas (3 veces)
                        frequencyTimes = listOf(
                            LocalTime.of(8, 0),
                            LocalTime.of(16, 0),
                            LocalTime.of(23, 0)
                        ),
                        // Inventario: 2 cajas de 12
                        unitsPerPackage = "12",
                        packageCount = "2",
                        isStockAlertEnabled = true,
                        // El ViewModel habría calculado esto:
                        stockAlertDescription = "Avisar cuando queden 9 pzas (aprox. 3 días)"
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
                        presentation = MedicationPresentation.CREAM, // Esto activa la estrategia BY_CONTAINER
                        name = "Betametasona",
                        concentration = "0.05%",
                        // Nota: dose y unit se ignoran en la UI para cremas
                        unitsPerPackage = "40", // 40g (Informativo)
                        packageCount = "2",     // 2 Tubos

                        // Instrucciones textuales en lugar de dosis numérica
                        instructions = "Aplicar una capa fina en la zona afectada tras el baño.",

                        isStockAlertEnabled = true,
                        // Lógica fija para envases:
                        stockAlertDescription = "Avisar cuando quede el último envase"
                    ),
                    onEvent = {},
                    widthSizeClass = WindowWidthSizeClass.Compact
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
                        dose = "5", // mL
                        unit = "mL",

                        // Frecuencia: Solo Lunes, Miércoles y Viernes
                        frequencyDays = setOf(1, 3, 5),
                        frequencyTimes = listOf(LocalTime.of(10, 0)),

                        unitsPerPackage = "120", // 120 mL botella
                        packageCount = "1",

                        // Indefinido activado
                        isIndefinite = true,

                        stockAlertDescription = "Avisar cuando queden 45 mL (aprox. 3 días)"
                    ),
                    onEvent = {},
                    widthSizeClass = WindowWidthSizeClass.Compact,
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
                        dose = "5", // mL
                        unit = "mL",

                        // Frecuencia: Solo Lunes, Miércoles y Viernes
                        frequencyDays = setOf(1, 3, 5),
                        frequencyTimes = listOf(LocalTime.of(10, 0)),

                        unitsPerPackage = "120", // 120 mL botella
                        packageCount = "1",

                        // Indefinido activado
                        isIndefinite = true,

                        stockAlertDescription = "Avisar cuando queden 45 mL (aprox. 3 días)"
                    ),
                    onEvent = {},
                    widthSizeClass = WindowWidthSizeClass.Expanded,
                )
            }
        }
    }
}
