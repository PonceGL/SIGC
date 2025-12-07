package com.poncegl.sigc.ui.components.onboarding.slider

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.poncegl.sigc.R
import com.poncegl.sigc.ui.feature.onboarding.model.OnboardingPage
import com.poncegl.sigc.ui.theme.SigcTheme

@Composable
fun Slider(
    currentPageIndex: Int,
    pages: List<OnboardingPage>
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

        SliderItem(
            painter = painterResource(id = pages[index].imageRes),
            title = pages[index].title,
            description = pages[index].description,
            iconColor = dynamicIconColor
        )
    }
}

@Preview
@Composable
fun SliderPreview() {
    Slider(
        currentPageIndex = 0,
        pages = listOf(
            OnboardingPage(
                imageRes = R.drawable.heart,
                title = "Cuidado Integral",
                description = "Centraliza toda la información de cuidados de tu paciente en un solo lugar. Medicamentos, signos vitales y más."
            )
        )
    )
}