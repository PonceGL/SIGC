package com.poncegl.sigc.ui.components.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.background
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Replace with actual logo
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = background,
            modifier = Modifier.size(70.dp),
//            shadowElevation = 4.dp
        ) {

            Box(contentAlignment = Alignment.Center) {

                Image(
                    painter = painterResource(id = R.drawable.nurse),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }

        }

//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.Start
//        ) {
//            Text(
//                text = "SIGC",
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//
//            Text(
//                text = "Sistema Integral de Gestión de Cuidados",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                textAlign = TextAlign.Center
//            )
//        }
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
                    .padding(24.dp)
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
                    .padding(24.dp)
            )
        }
    }
}