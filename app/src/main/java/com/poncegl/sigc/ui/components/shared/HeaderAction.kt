package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun HeaderAction(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    startIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    startIconDescription: String = "Regresar",
    startIconAction: () -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (startIcon != null) {
            IconButton(
                onClick = startIconAction,
            ) {
                Icon(
                    imageVector = startIcon,
                    contentDescription = startIconDescription,
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Left,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Left,
            )
        }
    }
}

@Preview(
    name = "1. Light Mode - Email (Con Icono)",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun PreviewSigcTextFieldLight() {
    SIGCTheme(darkTheme = false) {
        Surface {
            HeaderAction(
                title = "Header Action",
                description = "Header Action",
            )
        }
    }
}

@Preview(
    name = "2. Dark Mode - Password (Oculto)",
    device = "id:pixel_5", apiLevel = 31,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSigcTextFieldDarkPassword() {
    SIGCTheme(darkTheme = true) {
        Surface {
            HeaderAction(
                title = "Header Action",
                description = "Header Action",
            )
        }
    }
}