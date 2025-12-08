package com.poncegl.sigc.ui.components.onboarding.slider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R

@Composable
fun SlideIcon(painter: Painter, color: Color){
    Box(){
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(20))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Android icon launcher",
                colorFilter = ColorFilter.tint(
                    color = color,
                ),
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Fit
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HeartSlidePreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SlideIcon(painter = painterResource(id = R.drawable.heart), color = MaterialTheme.colorScheme.primary)
        SlideIcon(painter = painterResource(id = R.drawable.bell), color = Color.Red)
        SlideIcon(painter = painterResource(id = R.drawable.list), color = Color.Green)
        SlideIcon(painter = painterResource(id = R.drawable.people), color = Color.Blue)
    }
}
