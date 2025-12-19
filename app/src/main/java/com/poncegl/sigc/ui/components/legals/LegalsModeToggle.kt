package com.poncegl.sigc.ui.components.legals

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.ToggleButton

@Composable
fun LegalsModeToggle(
    modifier: Modifier = Modifier,
    isTerms: Boolean,
    changeMode: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        ToggleButton(
            text = "Términos de Servicio",
            isSelected = isTerms,
            onClick = { changeMode() },
            modifier = Modifier.weight(1f)
        )

        ToggleButton(
            text = "Política de Privacidad",
            isSelected = !isTerms,
            onClick = { changeMode() },
            modifier = Modifier.weight(1f)
        )
    }
}