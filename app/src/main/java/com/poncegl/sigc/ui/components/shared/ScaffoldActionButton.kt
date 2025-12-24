package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.ui.theme.SIGCTheme

sealed interface FabIcon {
    data class Vector(val imageVector: ImageVector) : FabIcon
    data class Drawable(@DrawableRes val id: Int) : FabIcon
}

@Composable
fun ScaffoldActionButton(
    modifier: Modifier = Modifier,
    icon: FabIcon = FabIcon.Vector(Icons.Filled.Add),
    widthSizeClass: WindowWidthSizeClass,
    label: String,
    onClick: () -> Unit,
) {

    if (widthSizeClass == WindowWidthSizeClass.Compact) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
        ) {
            IconContent(icon, label)
        }
    } else {
        ExtendedFloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
            icon = { IconContent(icon, label) },
            text = { Text(text = label) },
        )
    }
}

@Composable
private fun IconContent(icon: FabIcon, label: String) {
    when (icon) {
        is FabIcon.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = label
            )
        }

        is FabIcon.Drawable -> {
            Icon(
                painter = painterResource(id = icon.id),
                contentDescription = label
            )
        }
    }
}

@Preview(name = "1. Mobile Light", showBackground = true)
@Composable
private fun ScaffoldActionButtonLight() {
    SIGCTheme(darkTheme = false) {
        ScaffoldActionButton(
            widthSizeClass = WindowWidthSizeClass.Compact,
            icon = FabIcon.Vector(Icons.Filled.Add),
            label = "Nuevo",
            onClick = {}
        )
    }
}

@Preview(name = "2. Mobile Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScaffoldActionButtonDark() {
    SIGCTheme(darkTheme = true) {
        ScaffoldActionButton(
            widthSizeClass = WindowWidthSizeClass.Expanded,
            icon = FabIcon.Vector(Icons.Filled.Add),
            label = "Registrar paciente",
            onClick = {}
        )
    }
}
