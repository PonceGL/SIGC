package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
private fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.surface
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val elevation =
        if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        )

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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