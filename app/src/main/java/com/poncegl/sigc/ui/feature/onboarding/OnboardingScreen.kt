package com.poncegl.sigc.ui.feature.onboarding

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.BuildConfig
import com.poncegl.sigc.data.repository.UserPreferencesRepository
import com.poncegl.sigc.ui.components.onboarding.OnboardingContent
import com.poncegl.sigc.ui.feature.onboarding.model.onboardingPagesData
import com.poncegl.sigc.ui.theme.SIGCTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit = {}
) {
    val appName = BuildConfig.APP_NAME
    var currentPageIndex by rememberSaveable { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }

    val pages = onboardingPagesData
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = UserPreferencesRepository(context)

    fun completeOnboarding() {
        scope.launch {
            repository.saveOnboardingCompleted()
            onFinishOnboarding()
        }
    }

    fun onPreviousClick() {
        if (currentPageIndex > 0) currentPageIndex--
    }

    fun onNextClick() {
        if (currentPageIndex < pages.size - 1) {
            currentPageIndex++
        } else {
            completeOnboarding()
        }
    }

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
            title = { Text(text = "Salir de $appName") },
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

    OnboardingContent(
        currentPageIndex = currentPageIndex,
        pages = pages,
        prevAction = { onPreviousClick() },
        nextction = { onNextClick() },
        completeOnboarding = { completeOnboarding() }
    )

}

@Preview(showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    SIGCTheme(darkTheme = false) {
        OnboardingScreen(onFinishOnboarding = {
            println("Navegar a Home")
        })
    }
}
