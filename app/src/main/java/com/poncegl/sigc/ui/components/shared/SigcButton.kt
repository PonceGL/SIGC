package com.poncegl.sigc.ui.components.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.theme.SIGCTheme

enum class SigcButtonType {
    Primary,
    Outlined
}

@Composable
fun SigcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: SigcButtonType = SigcButtonType.Primary,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(12.dp)
    val height = 50.dp

    when (type) {
        SigcButtonType.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                ButtonContent(text, startIcon, endIcon)
            }
        }

        SigcButtonType.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled,
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                ButtonContent(text, startIcon, endIcon)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (startIcon != null) {
            Icon(
                imageVector = startIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (endIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = endIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// --- Previews ---

@Preview(showBackground = true)
@Composable
private fun PreviewSigcButtonPrimary() {
    SIGCTheme {
        SigcButton(
            text = "Guardar",
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSigcButtonOutlinedWithIcons() {
    SIGCTheme {
        SigcButton(
            text = "Continuar",
            onClick = {},
            type = SigcButtonType.Outlined,
            endIcon = Icons.AutoMirrored.Filled.ArrowForward,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSigcButtonBothIcons() {
    SIGCTheme {
        SigcButton(
            text = "Agregar Item",
            onClick = {},
            startIcon = Icons.Default.Add,
            endIcon = Icons.Default.Check,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}
