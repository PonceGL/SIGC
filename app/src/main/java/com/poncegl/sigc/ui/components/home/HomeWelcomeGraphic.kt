package com.poncegl.sigc.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.theme.SIGCTheme
import com.poncegl.sigc.ui.theme.SigcTheme

@Composable
fun HomeWelcomeGraphic(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Círculo central grande (Corazón)
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Círculo pequeño izquierda (PersonAdd)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = (-40).dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(SigcTheme.colors.success.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.people_plus),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = SigcTheme.colors.success
            )
        }

        // Círculo pequeño derecha (Group)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-20).dp, y = (-40).dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(SigcTheme.colors.medUpcoming.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.people),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = SigcTheme.colors.medUpcoming
            )
        }
    }
}

@Preview(name = "1. Mobile Light", showBackground = true)
@Composable
private fun HomeWelcomeGraphicPreview() {
    SIGCTheme(darkTheme = false) {
        HomeWelcomeGraphic()
    }
}