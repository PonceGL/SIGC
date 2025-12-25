package com.poncegl.sigc.ui.components.registerPatient

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poncegl.sigc.ui.components.shared.HeaderAction
import com.poncegl.sigc.ui.theme.SIGCTheme

sealed class Step(val route: String, val title: String) {
    object One : Step("step_one", "1")
    object Two : Step("step_two", "2")
    object Three : Step("step_three", "3")
    object Four : Step("step_four", "4")
}


@Composable
fun RegisterPatientContent(widthSizeClass: WindowWidthSizeClass) {


    val onBack = {
        Log.d("RegisterPatientContent", "onBack")
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HeaderAction(
                    title = "Datos del paciente",
                    description = "Información básica",
                    startIconAction = { onBack() }
                )

                NavigationTabExample()
            }
        }
    }

}

@Composable
fun NavigationTabExample() {
    val navController = rememberNavController()
    val steps = listOf(Step.One, Step.Two, Step.Three, Step.Four)
    var selectedIndex by remember { mutableIntStateOf(0) }

    RegisterPatientStepsBar(
        steps = steps,
        currentStepIndex = selectedIndex,
        onStepClick = { index ->
            selectedIndex = index
            navController.navigate(steps[index].route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    )

    NavHost(navController, startDestination = Step.One.route) {
        composable(Step.One.route) { StepOne() }
        composable(Step.Two.route) { StepTwo() }
        composable(Step.Three.route) { StepThree() }
        composable(Step.Four.route) { StepFour() }
    }
}

@Composable
fun RegisterPatientStepsBar(
    steps: List<Step>,
    currentStepIndex: Int,
    onStepClick: (Int) -> Unit
) {
    // Calculamos el progreso (ej. paso 1 de 4 = 0.25)
    val progressTarget = (currentStepIndex + 1) / steps.size.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        label = "StepProgressAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = index < currentStepIndex
                val isCurrent = index == currentStepIndex
                
                // Visualización del círculo de paso
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted || isCurrent) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onStepClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completado",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = step.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isCurrent) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepOne() {
    Text("Contenido de la pantalla de StepOne", modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Green))
}

@Composable
fun StepTwo() {
    Text("Contenido de la pantalla de StepTwo", modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Yellow))
}

@Composable
fun StepThree() {
    Text("Contenido de la pantalla de StepThree", modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Blue))
}

@Composable
fun StepFour() {
    Text("Contenido de la pantalla de StepFour", modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Magenta))
}


@Preview(name = "1. Mobile Light", device = "id:pixel_5", showBackground = true)
@Composable
private fun PreviewEmptyContentLight() {
    SIGCTheme(darkTheme = false) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact)
    }
}

@Preview(
    name = "2. Mobile Dark",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Compact)
    }
}

@Preview(
    name = "3. Foldable Dark",
    device = "id:pixel_fold",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentFoldDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Expanded)
    }
}

@Preview(
    name = "4. Tablet Dark",
    device = "id:pixel_tablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEmptyContentTabletDark() {
    SIGCTheme(darkTheme = true) {
        RegisterPatientContent(widthSizeClass = WindowWidthSizeClass.Expanded)
    }
}
