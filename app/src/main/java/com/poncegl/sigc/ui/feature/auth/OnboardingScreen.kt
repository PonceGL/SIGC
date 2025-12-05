package com.poncegl.sigc.ui.feature.auth

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.onboarding.NavigationButtons
import com.poncegl.sigc.ui.components.onboarding.OnboardingContent
import com.poncegl.sigc.ui.components.onboarding.OnboardingHeader
import com.poncegl.sigc.ui.components.shared.PageIndicator
import com.poncegl.sigc.ui.feature.auth.model.onboardingPagesData
import com.poncegl.sigc.ui.theme.SIGCTheme
import com.poncegl.sigc.ui.theme.SigcTheme

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit = {}
) {
    var currentPageIndex by rememberSaveable { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }

    val pages = onboardingPagesData
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (currentPageIndex > 0) {
            currentPageIndex--
        } else {
            showExitDialog = true
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = "Salir de SIGC") },
            text = { Text(text = "¿Estás seguro de que deseas salir de la aplicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val activity = context as? Activity
                        activity?.finish()
                    }
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        OnboardingHeader(
            onSkipClick = onNavigateToHome
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedContent(
                targetState = currentPageIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut())
                    }
                },
                label = "OnboardingAnimation"
            ) { index ->
                val dynamicIconColor = when (index) {
                    0 -> MaterialTheme.colorScheme.primary
                    1 -> SigcTheme.colors.warning
                    2 -> SigcTheme.colors.success
                    3 -> SigcTheme.colors.medUpcoming
                    else -> MaterialTheme.colorScheme.primary
                }

                OnboardingContent(
                    painter = painterResource(id = pages[index].imageRes),
                    title = pages[index].title,
                    description = pages[index].description,
                    iconColor = dynamicIconColor
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PageIndicator(
                pageSize = pages.size,
                selectedPage = currentPageIndex
            )
        }

        NavigationButtons(
            showPrevious = currentPageIndex > 0,
            onPreviousClick = {
                if (currentPageIndex > 0) currentPageIndex--
            },
            onNextClick = {
                if (currentPageIndex < pages.size - 1) {
                    currentPageIndex++
                } else {
                    onNavigateToHome()
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    SIGCTheme(darkTheme = false) {
        OnboardingScreen(onNavigateToHome = {
            println("Navegar a Home")
        })
    }
}
