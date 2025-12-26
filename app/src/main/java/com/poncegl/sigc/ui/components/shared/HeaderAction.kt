package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.theme.SIGCTheme

sealed interface HeaderIcon {
    data class Vector(val imageVector: ImageVector) : HeaderIcon
    data class Drawable(@DrawableRes val id: Int) : HeaderIcon
}

@Composable
fun HeaderAction(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    startIcon: HeaderIcon? = HeaderIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
    startIconDescription: String = "Regresar",
    startIconAction: () -> Unit = {},
    endIcon: HeaderIcon? = null,
    endIconDescription: String = "",
    endIconAction: () -> Unit = {}
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
                IconContent(
                    startIcon,
                    startIconDescription,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(1f)
        ) {
            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "HeaderTitleAnimation"
            ) { targetTitle ->
                Text(
                    text = targetTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Left,
                )
            }

            AnimatedContent(
                targetState = description,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "HeaderDescriptionAnimation"
            ) { targetDescription ->
                Text(
                    text = targetDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
            }
        }

        AnimatedContent(
            targetState = endIcon,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "HeaderIconAnimation"
        ) { targetIcon ->
            if (targetIcon != null) {
                IconButton(
                    onClick = endIconAction,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    IconContent(targetIcon, endIconDescription)
                }
            }
        }
    }
}

@Composable
private fun IconContent(
    icon: HeaderIcon,
    label: String = "",
    tint: Color = MaterialTheme.colorScheme.primary
) {
    when (icon) {
        is HeaderIcon.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = tint
            )
        }

        is HeaderIcon.Drawable -> {
            Icon(
                painter = painterResource(id = icon.id),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = tint
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
private fun PreviewHeaderActionLight() {
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
private fun PreviewHeaderActionDark() {
    SIGCTheme(darkTheme = true) {
        Surface {
            HeaderAction(
                title = "Header Action",
                description = "Header Action",
            )
        }
    }
}

@Preview(
    name = "3. Light Mode - Email (Con Icono)",
    device = "id:pixel_5",
    apiLevel = 31,
    showBackground = true
)
@Composable
private fun PreviewHeaderActionLightIcon() {
    SIGCTheme(darkTheme = false) {
        Surface {
            HeaderAction(
                title = "Header Action",
                description = "Header Action",
                endIcon = HeaderIcon.Vector(Icons.Filled.Add)
            )
        }
    }
}