package com.poncegl.sigc.ui.components.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.FabIcon
import com.poncegl.sigc.ui.components.shared.ScaffoldActionButton

data class FabMenuItem(
    val label: String,
    val icon: FabIcon,
    val contentDescription: String? = null,
    val onClick: () -> Unit
)

@Composable
fun FloatingActionMenu(
    widthSizeClass: WindowWidthSizeClass,
    items: List<FabMenuItem>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    MenuItemRow(item)
                }
            }
        }

        ScaffoldActionButton(
            widthSizeClass = widthSizeClass,
            label = "Registrar paciente",
            onClick = { expanded = !expanded }
        )
    }
}

@Composable
private fun MenuItemRow(item: FabMenuItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        SmallFloatingActionButton(onClick = item.onClick) {
            when (val icon = item.icon) {
                is FabIcon.Vector -> {
                    Icon(
                        imageVector = icon.imageVector,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = item.contentDescription
                    )
                }

                is FabIcon.Drawable -> {
                    Icon(
                        painter = painterResource(id = icon.id),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = item.contentDescription
                    )
                }
            }
        }
    }
}
