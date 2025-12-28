package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.medications.RegisterMedication
import com.poncegl.sigc.ui.components.shared.AddMedicationCard
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcButtonType
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun MedicationsData(
    widthSizeClass: WindowWidthSizeClass,
    isShowingMedicationForm: Boolean,
    onAddMedicationAction: () -> Unit,
    onBackAction: () -> Unit,
    onContinueAction: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .widthIn(max = UI.MAX_WIDTH.dp)
            .fillMaxSize(),
    ) {
        if (isShowingMedicationForm) {
            RegisterMedication(
                widthSizeClass = widthSizeClass,
                onRegisterMedication = {}
            )
        } else {

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Agrega los medicamentos que toma el paciente. Puedes agregar más después.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )

                AddMedicationCard(onAction = onAddMedicationAction)
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SigcButton(
                    text = "Anterior",
                    onClick = { onBackAction() },
                    modifier = Modifier.weight(1f),
                    type = SigcButtonType.Outlined,
                    startIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
                )

                SigcButton(
                    text = "Siguiente",
                    onClick = { onContinueAction() },
                    modifier = Modifier.weight(1f),
                    type = SigcButtonType.Primary,
                    endIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                )
            }
        }
    }
}

@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun MedicationsDataLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        MedicationsData(
                            widthSizeClass = WindowWidthSizeClass.Compact,
                            isShowingMedicationForm = false,
                            onAddMedicationAction = {},
                            onBackAction = {},
                            onContinueAction = {}
                        )
                    }
                }
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
private fun MedicationsDataDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        MedicationsData(
                            widthSizeClass = WindowWidthSizeClass.Compact,
                            isShowingMedicationForm = false,
                            onAddMedicationAction = {},
                            onBackAction = {},
                            onContinueAction = {}
                        )
                    }
                }
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
private fun MedicationsDataFoldDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        MedicationsData(
                            widthSizeClass = WindowWidthSizeClass.Expanded,
                            isShowingMedicationForm = true,
                            onAddMedicationAction = {},
                            onBackAction = {},
                            onContinueAction = {}
                        )
                    }
                }
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
private fun MedicationsDataTabletDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        MedicationsData(
                            widthSizeClass = WindowWidthSizeClass.Expanded,
                            isShowingMedicationForm = false,
                            onAddMedicationAction = {},
                            onBackAction = {},
                            onContinueAction = {}
                        )
                    }
                }
            }
        }
    }
}
