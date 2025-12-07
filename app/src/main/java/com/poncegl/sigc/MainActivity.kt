package com.poncegl.sigc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.poncegl.sigc.data.repository.UserPreferencesRepository
import com.poncegl.sigc.ui.MainViewModel
import com.poncegl.sigc.ui.navigation.SigcNavHost
import com.poncegl.sigc.ui.theme.SIGCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferencesRepository = UserPreferencesRepository(applicationContext)

        val viewModel: MainViewModel by viewModels {
            MainViewModel.provideFactory(userPreferencesRepository)
        }

        setContent {
            SIGCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading by viewModel.isLoading.collectAsState()
                    val startDestination by viewModel.startDestination.collectAsState()

                    if (isLoading) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            CircularProgressIndicator()
                        }
                    } else {
                        SigcNavHost(startDestination = startDestination)
                    }
                }
            }
        }
    }
}
