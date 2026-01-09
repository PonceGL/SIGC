package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.feature.auth.login.AuthMode
import com.poncegl.sigc.ui.feature.auth.login.LoginUiState
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    background: Color = MaterialTheme.colorScheme.background
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Replace with actual logo
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = background,
            modifier = Modifier.size(70.dp),
        ) {

            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.sigc_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }

        }

        if (state.authMode != AuthMode.RECOVER_PASSWORD) {
            Text(
                text = if (state.authMode == AuthMode.LOGIN) "¡Bienvenido!" else "Crea tu cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Preview(
    name = "1. Light Mode - Header",
    device = "id:pixel_5", apiLevel = 31,
    showBackground = true,
    backgroundColor = 0xFFF9FAFB
)
@Composable
private fun PreviewLoginHeaderLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            LoginHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                state = LoginUiState(authMode = AuthMode.LOGIN)
            )
        }
    }
}

@Preview(
    name = "2. Dark Mode - Header",
    device = "id:pixel_5", apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF101217
)
@Composable
private fun PreviewLoginHeaderDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            LoginHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                state = LoginUiState(authMode = AuthMode.LOGIN)
            )
        }
    }
}