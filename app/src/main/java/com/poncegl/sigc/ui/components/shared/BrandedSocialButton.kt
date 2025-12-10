package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
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
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun BrandedSocialButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    iconPlaceholder: @Composable () -> Unit,
    onClick: () -> Unit,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(0.dp)
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.CenterStart) {
                iconPlaceholder()
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
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
private fun BrandedSocialButtonPreview() {
    SIGCTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BrandedSocialButton(
                text = "Continuar con Google",
                containerColor = Color.White,
                contentColor = Color.Black,
                iconPlaceholder = {
                    Row {
                        Text(
                            "G",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                },
                onClick = {}
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
private fun BrandedSocialButtonPreviewDark() {
    SIGCTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BrandedSocialButton(
                text = "Continue with Facebook",
                containerColor = Color(0xFF1877F2),
                contentColor = Color.White,
                iconPlaceholder = {
                    // TODO: Reemplazar con Image(painterResource(R.drawable.ic_facebook), ...)
                    Text("f", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
                },
                onClick = {}
            )

        }
    }
}