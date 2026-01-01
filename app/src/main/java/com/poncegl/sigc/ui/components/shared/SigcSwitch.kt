package com.poncegl.sigc.ui.components.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun SigcSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            checkedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),

            uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            uncheckedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        )
    )
}

@Preview(name = "1. Mobile Light")
@Composable
fun SigcSwitchLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            SigcSwitch(
                checked = true,
                onCheckedChange = {},
                modifier = Modifier,
                enabled = true

            )
        }
    }
}

@Preview(name = "1. Mobile Light")
@Composable
fun SigcSwitchDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            SigcSwitch(
                checked = true,
                onCheckedChange = {},
                modifier = Modifier,
                enabled = true

            )
        }
    }
}