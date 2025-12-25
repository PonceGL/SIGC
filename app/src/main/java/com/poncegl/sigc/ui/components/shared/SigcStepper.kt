package com.poncegl.sigc.ui.components.shared

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poncegl.sigc.ui.theme.SIGCTheme

enum class StepStatus {
    COMPLETED,
    CURRENT,
    PENDING
}

/**
 * Componente de barra de progreso por pasos (Stepper).
 *
 * @param steps Lista genérica de pasos.
 * @param currentStepIndex Índice del paso actual (base 0).
 * @param onStepClick Callback al hacer click en un paso.
 * @param isNavigationEnabled Si es false, los clicks en los pasos son ignorados (útil para obligar a terminar el paso actual).
 * @param stepIndicator Composable que renderiza cada indicador individual.
 */
@Composable
fun <T> SigcStepper(
    modifier: Modifier = Modifier,
    steps: List<T>,
    currentStepIndex: Int,
    onStepClick: (Int) -> Unit,
    isNavigationEnabled: Boolean = true,
    stepIndicator: @Composable (step: T, index: Int, status: StepStatus) -> Unit
) {
    if (steps.isEmpty()) return

    val progressTarget = (currentStepIndex + 1) / steps.size.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        label = "StepProgressAnimation"
    )

    Column(
        modifier = modifier
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
                val status = when {
                    index < currentStepIndex -> StepStatus.COMPLETED
                    index == currentStepIndex -> StepStatus.CURRENT
                    else -> StepStatus.PENDING
                }

                Box(
                    modifier = Modifier
                        .clickable(enabled = isNavigationEnabled) { onStepClick(index) }
                ) {
                    stepIndicator(step, index, status)
                }
            }
        }
    }
}

@Composable
fun SigcStepCircle(
    text: String,
    status: StepStatus,
) {
    val backgroundColor = when (status) {
        StepStatus.COMPLETED, StepStatus.CURRENT -> MaterialTheme.colorScheme.primary
        StepStatus.PENDING -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (status) {
        StepStatus.COMPLETED, StepStatus.CURRENT -> MaterialTheme.colorScheme.onPrimary
        StepStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (status == StepStatus.COMPLETED) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completado",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSigcStepper() {
    val steps = listOf("Datos", "Info", "Pago", "Final")
    SIGCTheme {
        Column(Modifier.padding(16.dp)) {
            SigcStepper(
                steps = steps,
                currentStepIndex = 1,
                onStepClick = {},
                isNavigationEnabled = true
            ) { step, index, status ->
                SigcStepCircle(text = (index + 1).toString(), status = status)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSigcStepperCustom() {
    val steps = listOf("A", "B", "C")
    SIGCTheme {
        Column(Modifier.padding(16.dp)) {
            Text("Ejemplo con texto personalizado:")
            SigcStepper(
                steps = steps,
                currentStepIndex = 0,
                onStepClick = {},
                isNavigationEnabled = true
            ) { step, _, status ->
                val color = if (status == StepStatus.CURRENT) Color.Blue else Color.Gray
                Text(
                    text = step,
                    color = color,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
