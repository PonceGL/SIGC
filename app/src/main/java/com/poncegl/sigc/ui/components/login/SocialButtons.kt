package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poncegl.sigc.ui.components.shared.BrandedSocialButton
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun SocialButtons(
    modifier: Modifier = Modifier,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = "O continúa con",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BrandedSocialButton(
                text = "Continue with Facebook",
                containerColor = Color(0xFF1877F2),
                contentColor = Color.White,
                iconPlaceholder = {
                    // TODO: Reemplazar con Image(painterResource(R.drawable.ic_facebook), ...)
                    Text("f", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
                },
                onClick = onFacebookClick
            )

            BrandedSocialButton(
                text = "Continue with Google",
                containerColor = Color.White,
                contentColor = Color(0xFF757575),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                iconPlaceholder = {
                    // TODO: Reemplazar con Image(painterResource(R.drawable.ic_google_colored), ...)
                    Row {
                        Text(
                            "G",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                },
                onClick = onGoogleClick
            )
        }
    }
}

@Preview(
    name = "Light Mode - Stacked Buttons",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun PreviewSocialButtonsStackedLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            SocialButtons(
                modifier = Modifier.padding(16.dp),
                onGoogleClick = {},
                onFacebookClick = {},
            )
        }
    }
}

@Preview(
    name = "Dark Mode - Stacked Buttons",
    device = "id:pixel_5", apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSocialButtonsStackedDark() {
    SIGCTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) { // Asegurar fondo oscuro
            SocialButtons(
                modifier = Modifier.padding(16.dp),
                onGoogleClick = {},
                onFacebookClick = {},
            )
        }
    }
}