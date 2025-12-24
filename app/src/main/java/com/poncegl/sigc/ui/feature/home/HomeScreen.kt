package com.poncegl.sigc.ui.feature.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.poncegl.sigc.ui.components.home.EmptyContent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    widthSizeClass: WindowWidthSizeClass,
    onNavigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    rememberListDetailPaneScaffoldNavigator<Any>()
    rememberCoroutineScope()
    BackNavigationBehavior.PopUntilScaffoldValueChange

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                HomeUiEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    EmptyContent(widthSizeClass)

//    Scaffold(
//        containerColor = MaterialTheme.colorScheme.background
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(vertical = 10.dp)
//                .imePadding(),
//            contentAlignment = Alignment.Center
//        ) {
//
//            NavigableListDetailPaneScaffold(
//                navigator = scaffoldNavigator,
//                listPane = {
//                    AnimatedPane {
//                        HomeListPane(
//                            items = mockHomeItems,
//                            onItemClick = { item ->
//                                scope.launch {
//                                    scaffoldNavigator.navigateTo(
//                                        ListDetailPaneScaffoldRole.Detail,
//                                        item
//                                    )
//                                }
//                            },
//                            onLogoutClick = { showLogoutDialog = true }
//                        )
//                    }
//                },
//                detailPane = {
//                    AnimatedPane {
//                        // Se hace un cast seguro a HomeItem ya que contentKey es de tipo Any?
//                        val selectedItem =
//                            scaffoldNavigator.currentDestination?.content?.let { it as? HomeItem }
//
//                        // Determina si se debe mostrar el botón de cerrar/atrás
//                        val showCloseButton =
//                            scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
//
//                        HomeDetailPane(
//                            item = selectedItem,
//                            showCloseButton = showCloseButton,
//                            onClose = {
//                                scope.launch {
//                                    scaffoldNavigator.navigateBack(backNavigationBehavior)
//                                }
//                            }
//                        )
//                    }
//                },
//            )
//
//            if (showLogoutDialog) {
//                AlertDialog(
//                    onDismissRequest = { showLogoutDialog = false },
//                    title = { Text(text = "Cerrar Sesión") },
//                    text = { Text(text = "¿Estás seguro de que deseas salir de la aplicación?") },
//                    confirmButton = {
//                        TextButton(
//                            onClick = {
//                                showLogoutDialog = false
//                                viewModel.onLogoutConfirmed()
//                            }
//                        ) {
//                            Text("Salir")
//                        }
//                    },
//                    dismissButton = {
//                        TextButton(
//                            onClick = { showLogoutDialog = false }
//                        ) {
//                            Text("Cancelar")
//                        }
//                    }
//                )
//            }
//        }
//
//    }

}
