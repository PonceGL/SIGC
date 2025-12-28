package com.poncegl.sigc.ui.components.medications

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun RegisterMedication(
    widthSizeClass: WindowWidthSizeClass,
    onRegisterMedication: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .widthIn(max = UI.MAX_WIDTH.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SigcTextField(
                value = "",
                onValueChange = { },
                label = "Nombre del medicamento",
                placeholder = "Ejemplo: Paracetamol",
                keyboardType = KeyboardType.Text,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SigcTextField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = { },
                    label = "Dosis",
                    placeholder = "500",
                    keyboardType = KeyboardType.Number,
                )

                SigcTextField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = { },
                    label = "Unidad",
                    placeholder = "mg",
                    keyboardType = KeyboardType.Text,
                )
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿A qué horas se toma?",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Left,
                    )

                    TextButton(
                        onClick = {
                            // TODO:
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Agregar hora",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                SigcTextField(
                    value = "",
                    onValueChange = { },
                    label = "",
                    keyboardType = KeyboardType.Text, // TODO: selector de hora
                )
            }

            SigcTextField(
                value = "",
                onValueChange = { },
                label = "¿Por cuántos días?",
                placeholder = "7",
                keyboardType = KeyboardType.Number,
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
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
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Left,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SigcTextField(
                            modifier = Modifier.weight(1f),
                            value = "",
                            onValueChange = { },
                            label = "Unidades por caja",
                            placeholder = "10",
                            keyboardType = KeyboardType.Number,
                        )

                        SigcTextField(
                            modifier = Modifier.weight(1f),
                            value = "",
                            onValueChange = { },
                            label = "Cajas disponibles",
                            placeholder = "1",
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Avisar cuando se termine",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Left,
                            )

                            Text(
                                text = "Recibe una alerta antes de quedarte sin medicamento",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Left,
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Switch(
                                checked = true,
                                onCheckedChange = {
                                    // TODO:
                                }
                            )
                        }
                    }
                }
            }

            SigcTextField(
                value = "",
                onValueChange = { },
                label = "Indicación especial (opcional)",
                placeholder = "Ejemplo: Tomar en ayunas",
                keyboardType = KeyboardType.Text,
            )

            SigcTextField(
                value = "",
                onValueChange = { },
                label = "¿Para qué es? (opcional)",
                placeholder = "Ejemplo: Para el dolor",
                keyboardType = KeyboardType.Text,
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        SigcButton(
            text = "Guardar medicamento",
            onClick = {
                onRegisterMedication()
            },
            modifier = Modifier.fillMaxWidth(),
            startIcon = Icons.Default.Check,
        )
    }

}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun RegisterMedicationsLight() {
    SIGCTheme(darkTheme = false) {
        Surface {

            RegisterMedication(
                widthSizeClass = WindowWidthSizeClass.Compact,
                onRegisterMedication = {}
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
                widthSizeClass = WindowWidthSizeClass.Compact,
                onRegisterMedication = {}
            )
        }
    }
}
