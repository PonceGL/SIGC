package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun ScaffoldActionButton(widthSizeClass: WindowWidthSizeClass, onClick: () -> Unit) {
    if (widthSizeClass == WindowWidthSizeClass.Compact) {
        FloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
        ) {
            ScaffoldActionIcon()
        }
    } else {

        ExtendedFloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
            icon = {
                ScaffoldActionIcon()
            },
            text = { Text(text = "Registrar paciente") },
        )
    }
}

@Composable
private fun ScaffoldActionIcon() {
    Icon(
        imageVector = Icons.Filled.Add,
        contentDescription = "Boón para registrar paciente nuevo"
    )
}

@Preview(name = "1. Mobile Light", showBackground = true)
@Composable
private fun ScaffoldActionButtonLight() {
    SIGCTheme(darkTheme = false) {
        ScaffoldActionButton(widthSizeClass = WindowWidthSizeClass.Compact, onClick = {})
    }
}

@Preview(name = "2. Mobile Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScaffoldActionButtonDark() {
    SIGCTheme(darkTheme = true) {
        ScaffoldActionButton(widthSizeClass = WindowWidthSizeClass.Expanded, onClick = {})
    }
}