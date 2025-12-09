package com.poncegl.sigc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.poncegl.sigc.ui.MainViewModel
import com.poncegl.sigc.ui.navigation.SigcNavHost
import com.poncegl.sigc.ui.theme.SIGCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        setContent {
            SIGCTheme {
                val startDestination by viewModel.startDestination.collectAsState()

                @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
                val windowSize = calculateWindowSizeClass(this)

                SigcNavHost(
                    startDestination = startDestination,
                    windowSize = windowSize.widthSizeClass
                )
            }
        }
    }
}
