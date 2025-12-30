package com.poncegl.sigc.ui.components.medication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.SigcModalBottomSheet
import com.poncegl.sigc.ui.feature.patients.domain.model.MedicationPresentation

@Composable
fun MedicationPresentationSheet(
    selectedPresentation: MedicationPresentation,
    onPresentationSelected: (MedicationPresentation) -> Unit,
    onDismiss: () -> Unit
) {
    SigcModalBottomSheet(
        title = "Tipo de medicamento",
        onDismiss = onDismiss
    ) {
        LazyColumn {
            items(MedicationPresentation.values()) { presentation ->
                PresentationItem(
                    presentation = presentation,
                    isSelected = presentation == selectedPresentation,
                    onClick = {
                        onPresentationSelected(presentation)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun PresentationItem(
    presentation: MedicationPresentation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Icon(presentation.icon, ...)

        Text(
            text = presentation.label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Seleccionado",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
