package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.core.constants.UI
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcCallout
import com.poncegl.sigc.ui.components.shared.SigcTextField
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun PatientData(widthSizeClass: WindowWidthSizeClass, onContinueAction: () -> Unit) {

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
                value = "",
                onValueChange = {
                    Log.i("PatientData", "PatientData: $it")
                },
                label = "¿Cómo se llama el paciente?",
                keyboardType = KeyboardType.Text,
            )

            SigcTextField(
                value = "",
                onValueChange = {
                    Log.i("PatientData", "PatientData: $it")
                },
                label = "¿Cuántos años tiene?",
                keyboardType = KeyboardType.Number,
            )

            SigcTextField(
                value = "",
                onValueChange = {
                    Log.i("PatientData", "PatientData: $it")
                },
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
//                enabled = // TODO: Validar campos
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
