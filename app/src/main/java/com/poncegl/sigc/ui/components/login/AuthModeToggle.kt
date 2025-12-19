package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.shared.ToggleButton
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun AuthModeToggle(
    modifier: Modifier = Modifier,
    currentMode: AuthMode,
    onModeSelected: (AuthMode) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        ToggleButton(
            text = "Iniciar Sesión",
            isSelected = currentMode == AuthMode.LOGIN,
            onClick = { onModeSelected(AuthMode.LOGIN) },
            modifier = Modifier.weight(1f)
        )

        ToggleButton(
            text = "Registrarse",
            isSelected = currentMode == AuthMode.REGISTER,
            onClick = { onModeSelected(AuthMode.REGISTER) },
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview(
    name = "1. Light Mode - Login Selected",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun PreviewToggleLightLogin() {
    SIGCTheme(darkTheme = false) {
        Surface {
            AuthModeToggle(
                currentMode = AuthMode.LOGIN,
                onModeSelected = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    name = "2. Dark Mode - Register Selected",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewToggleDarkRegister() {
    SIGCTheme(darkTheme = true) {
        Surface {
            AuthModeToggle(
                currentMode = AuthMode.REGISTER,
                onModeSelected = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}