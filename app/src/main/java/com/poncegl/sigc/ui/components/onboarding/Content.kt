package com.poncegl.sigc.ui.components.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.components.onboarding.slider.Slider
import com.poncegl.sigc.ui.components.shared.PageIndicator
import com.poncegl.sigc.ui.feature.onboarding.model.OnboardingPage

@Composable
fun OnboardingContent(
    currentPageIndex: Int,
    pages: List<OnboardingPage>,
    prevAction: () -> Unit,
    nextAction: () -> Unit,
    completeOnboarding: () -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            OnboardingHeader(
                onSkipClick = {
                    completeOnboarding()
                }
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Slider(
                    currentPageIndex = currentPageIndex,
                    pages = pages
                )

                Spacer(modifier = Modifier.height(32.dp))

                PageIndicator(
                    pageSize = pages.size,
                    selectedPage = currentPageIndex
                )
            }

            NavigationButtons(
                showPrevious = currentPageIndex > 0,
                isLast = currentPageIndex == pages.size - 1,
                onPreviousClick = prevAction,
                onNextClick = nextAction
            )
        }
    }
}

@Preview
@Composable
fun OnboardingContentPreview() {
    OnboardingContent(
        currentPageIndex = 0,
        pages = listOf(
            OnboardingPage(
                imageRes = R.drawable.heart,
                title = "Cuidado Integral",
                description = "Centraliza toda la información de cuidados de tu paciente en un solo lugar. Medicamentos, signos vitales y más."
            )
        ),
        prevAction = {},
        nextAction = {},
        completeOnboarding = {}
    )
}