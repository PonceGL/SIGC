package com.poncegl.sigc.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> SigcChipGroup(
    items: List<T>,
    onItemClick: (T) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier,
    leadingIconProvider: ((T) -> androidx.compose.ui.graphics.vector.ImageVector)? = null,
    trailingIconProvider: ((T) -> androidx.compose.ui.graphics.vector.ImageVector)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SigcInputChip(
                modifier = Modifier.fillMaxWidth(),
                label = labelProvider(item),
                onClick = { onItemClick(item) },
                selected = true,
                leadingIcon = leadingIconProvider?.invoke(item),
                trailingIcon = trailingIconProvider?.invoke(item)
                    ?: androidx.compose.material.icons.Icons.Default.Close
            )
        }
    }
}


@Preview
@Composable
fun SigcChipGroupPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")

    SigcChipGroup(
        items = items,
        onItemClick = { },
        labelProvider = { it }

    )
}