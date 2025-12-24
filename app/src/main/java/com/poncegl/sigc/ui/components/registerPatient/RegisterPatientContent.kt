package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.HeaderAction
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun RegisterPatientContent(widthSizeClass: WindowWidthSizeClass) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                HeaderAction(
                    title = "Header Action",
                    description = "Header Action",
                )
                Text(text = "Register Patient Content")
            }
        }
    }

}


@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun PreviewEmptyContentLight() {
    SIGCTheme(darkTheme = false) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact)
    }
}

@Preview(
    name = "2. Mobile Dark",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact)
    }
}

@Preview(
    name = "3. Foldable Dark",
    device = "id:pixel_fold",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentFoldDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Expanded)
    }
}

@Preview(
    name = "4. Tablet Dark",
    device = "id:pixel_tablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentTabletDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Expanded)
    }
}