package com.poncegl.sigc.ui.components.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigcInputChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector = Icons.Default.Close,
) {
    InputChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        avatar = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        label = {
            Text(
                text = label,
                modifier = Modifier.padding(vertical = 10.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        trailingIcon = {
            Icon(
                imageVector = trailingIcon,
                contentDescription = "Action",
                modifier = Modifier.size(16.dp)
            )
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview
@Composable
fun SigcInputChipPreview() {
    SigcInputChip(
        label = "Item 1",
        onClick = { },
    )
}

@Preview
@Composable
fun SigcInputChipPreviewWithIcon() {
    SigcInputChip(
        label = "Item 1",
        onClick = { },
        leadingIcon = Icons.Default.Timer

    )
}