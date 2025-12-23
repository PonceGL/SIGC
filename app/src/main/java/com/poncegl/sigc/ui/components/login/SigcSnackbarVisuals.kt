package com.poncegl.sigc.ui.components.login

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

enum class SnackbarType {
    ERROR,
    SUCCESS,
    WARNING,
    INFO
}

data class SigcSnackbarVisuals(
    override val message: String,
    val type: SnackbarType = SnackbarType.INFO,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals
