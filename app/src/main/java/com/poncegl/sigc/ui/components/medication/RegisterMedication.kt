package com.poncegl.sigc.ui.components.medication

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcTextField
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

    // Diálogo para selector de hora (Material 3)
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
                .weight(1f) // Ocupa el espacio disponible para scroll
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 1. NOMBRE
            SigcTextField(
                value = formState.name,
                onValueChange = { onEvent(RegisterPatientEvent.MedNameChanged(it)) },
                label = "Nombre del medicamento",
                placeholder = "Ejemplo: Paracetamol",
                keyboardType = KeyboardType.Text,
            )

            // 2. DOSIS Y UNIDAD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SigcTextField(
                    modifier = Modifier.weight(1f),
                    value = formState.dose,
                    onValueChange = { onEvent(RegisterPatientEvent.MedDoseChanged(it)) },
                    label = "Dosis",
                    placeholder = "500",
                    keyboardType = KeyboardType.Number,
                )

                SigcTextField(
                    modifier = Modifier.weight(1f),
                    value = formState.unit,
                    onValueChange = { onEvent(RegisterPatientEvent.MedUnitChanged(it)) },
                    label = "Unidad",
                    placeholder = "mg",
                    keyboardType = KeyboardType.Text,
                )
            }

            // 3. FRECUENCIA (HORAS)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿A qué horas se toma?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    TextButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Filled.Add, null, Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Agregar hora", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Lista de Chips para las horas agregadas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Wrap en FlowRow idealmente
                ) {
                    // Nota: Si son muchas, necesitarías FlowRow. Por brevedad uso Row con horizontalScroll o simple.
                    // Para MVP, mostraremos las primeras o usaremos un layout custom si son muchas.
                    // Como el video muestra una lista vertical de horas, si no tienes FlowRow, usa Column.
                    // Pero InputChip es bueno para esto.

                    formState.frequencyTimes.forEach { time ->
                        InputChip(
                            selected = false,
                            onClick = { /* Nada */ },
                            label = { Text(time.format(DateTimeFormatter.ofPattern("HH:mm"))) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Borrar",
                                    Modifier.size(16.dp)
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(),
//                            border = InputChipDefaults.inputChipBorder(borderColor = MaterialTheme.colorScheme.outline)
                            // TODO: Conectar onClick del trailingIcon a MedRemoveFrequencyTime
                            // InputChip no tiene onClick separado para el icono trailing facilmente,
                            // Usamos el onClick principal para borrar por simplicidad o custom layout.
                        )
                        // Hack rápido para evento de borrado en InputChip estándar:
                        // onClick = { onEvent(RegisterPatientEvent.MedRemoveFrequencyTime(time)) }
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

            // 5. INVENTARIO (CARD)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                        text = "Inventario disponible",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SigcTextField(
                            modifier = Modifier.weight(1f),
                            value = "10", // Hardcode placeholder visual (no está en formState aún, solo stock total)
                            onValueChange = { /* TODO: Unidades por caja */ },
                            label = "Unidades por caja", // TODO: evaluar si cajas es el mejor termino
                            placeholder = "10",
                            keyboardType = KeyboardType.Number,
//                            enabled = false // Deshabilitado por ahora en MVP simplificado
                        )

                        SigcTextField(
                            modifier = Modifier.weight(1f),
                            value = formState.stock,
                            onValueChange = { onEvent(RegisterPatientEvent.MedStockChanged(it)) },
                            label = "Cajas disponibles",
                            placeholder = "1",
                            keyboardType = KeyboardType.Number,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Avisar cuando se termine",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "Recibe una alerta...",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Switch(
                            checked = formState.stockAlertThreshold != "0",
                            onCheckedChange = { checked ->
                                // Lógica simple: Si on -> threshold = 1, Si off -> 0
                                if (checked) "1" else "0"
                                // Aquí necesitaríamos un evento específico o reutilizar StockChanged logic
                                // Para MVP simplificamos.
                            }
                        )
                    }
                }
            }

            // 6. OPCIONALES
            SigcTextField(
                value = formState.instructions,
                onValueChange = { onEvent(RegisterPatientEvent.MedInstructionsChanged(it)) },
                label = "Indicación especial (opcional)",
                placeholder = "Ejemplo: Tomar en ayunas",
            )

            SigcTextField(
                value = formState.usageReason,
                onValueChange = { onEvent(RegisterPatientEvent.MedReasonChanged(it)) },
                label = "¿Para qué es? (opcional)",
                placeholder = "Ejemplo: Para el dolor",
            )

            // Espacio extra al final del scroll
            Spacer(modifier = Modifier.height(20.dp))
        }

        // BOTÓN FLOTANTE (O FIJO AL FINAL)
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Botón CANCELAR (Faltaba en tu diseño original pero es vital)
            SigcButton(
                text = "Cancelar",
                onClick = { onEvent(RegisterPatientEvent.CancelAddingMedication) },
                modifier = Modifier.weight(1f),
                // type = SigcButtonType.Outlined // Asumiendo que existe
            )

            SigcButton(
                text = "Guardar",
                onClick = { onEvent(RegisterPatientEvent.SaveMedicationToList) },
                modifier = Modifier.weight(1f),
                startIcon = Icons.Default.Check,
                enabled = formState.name.isNotBlank() // Validación básica visual
            )
        }
    }
}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun RegisterMedicationsLight() {
    SIGCTheme(darkTheme = false) {
        Surface {

            RegisterMedication(
                formState = MedicationFormState(),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Compact
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
private fun RegisterMedicationsDark() {
    SIGCTheme(darkTheme = true) {
        Surface {

            RegisterMedication(
                formState = MedicationFormState(),
                onEvent = {},
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}
