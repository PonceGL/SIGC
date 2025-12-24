package com.poncegl.sigc.ui.components.shared

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun BasicHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.size(45.dp),
        ) {

            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.nurse),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(fraction = 0.8f),
                    contentScale = ContentScale.Fit
                )
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { },
            ) {
                Icon(
                    painter = painterResource(R.drawable.bell),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Botón de notificaciones"
                )
            }

            IconButton(
                onClick = { },
            ) {
                Icon(
                    painter = painterResource(R.drawable.engine),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Botón de configuración"
                )
            }
        }
    }
}

@Preview(name = "1. Mobile Light", showBackground = true)
@Composable
private fun BasicHeaderPreviewLight() {
    SIGCTheme(darkTheme = false) {
        BasicHeader()
    }
}

@Preview(name = "2. Mobile Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BasicHeaderPreviewDark() {
    SIGCTheme(darkTheme = true) {
        BasicHeader()
    }
}