package com.poncegl.sigc.ui.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.components.onboarding.NavigationButtons
import com.poncegl.sigc.ui.components.onboarding.OnboardingContent
import com.poncegl.sigc.ui.components.onboarding.OnboardingHeader
import com.poncegl.sigc.ui.theme.SIGCTheme

@Composable
fun OnboardingScreen() {

    var showPrevious by rememberSaveable { mutableStateOf(false) }
    var counter by rememberSaveable { mutableIntStateOf(0) }


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
            onSkipClick = { /* TODO: Navigate to Home/Login */ }
        )

        OnboardingContent(
            title = "Cuidado Integral $counter",
            description = "Centraliza toda la información de cuidados de tu paciente en un solo lugar. Medicamentos, signos vitales y más."
        )

        NavigationButtons(
            showPrevious = showPrevious,
            onPreviousClick = {
                if (counter > 0) counter--
                if (counter == 0) showPrevious = false
            },
            onNextClick = {
                counter++
                showPrevious = true
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    SIGCTheme(darkTheme = false) {
        OnboardingScreen()
    }
}
