package com.poncegl.sigc.ui.components.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.SigcButton
import com.poncegl.sigc.ui.components.shared.SigcButtonType
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun NavigationButtons(
    modifier: Modifier = Modifier,
    showPrevious: Boolean,
    isLast: Boolean = false,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        if (showPrevious) {
            SigcButton(
                text = "Anterior",
                onClick = onPreviousClick,
                modifier = Modifier.weight(1f),
                type = SigcButtonType.Outlined,
                startIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
            )
        }

        SigcButton(
            text = if (isLast) "Comenzar" else "Siguiente",
            onClick = onNextClick,
            modifier = Modifier.weight(1f),
            type = SigcButtonType.Primary,
            endIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
        )
    }
}

@Preview
@Composable
fun NavigationButtonsPreview() {
    SIGCTheme(darkTheme = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationButtons(
                showPrevious = true,
                isLast = true,
                onPreviousClick = {},
                onNextClick = {}
            )

            NavigationButtons(
                showPrevious = false,
                isLast = false,
                onPreviousClick = {},
                onNextClick = {}
            )
        }
    }
}
