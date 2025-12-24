package com.poncegl.sigc.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class HomeItem(val id: Int, val title: String, val description: String)

val mockHomeItems = List(10) { i ->
    HomeItem(
        id = i,
        title = "Elemento $i",
        description = "Esta es la descripción detallada del elemento #$i. Aquí puedes mostrar más información relevante para el usuario."
    )
}

@Composable
fun HomeListPane(
    items: List<HomeItem>,
    onItemClick: (HomeItem) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Lista de Elementos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }

        items(items) { item ->
            Card(
                onClick = { onItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun HomeDetailPane(
    item: HomeItem?,
    onClose: () -> Unit,
    showCloseButton: Boolean,
    modifier: Modifier = Modifier
) {
    if (item == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Selecciona un elemento para ver detalles",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (showCloseButton) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar detalles")
                }
            }
            
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(name = "Mobile - Lista", showBackground = true, device = Devices.PHONE)
@Composable
fun PreviewHomeListMobile() {
    MaterialTheme {
        HomeListPane(
            items = mockHomeItems,
            onItemClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Mobile - Detalle", showBackground = true, device = Devices.PHONE)
@Composable
fun PreviewHomeDetailMobile() {
    MaterialTheme {
        HomeDetailPane(
            item = mockHomeItems.first(),
            onClose = {},
            showCloseButton = true
        )
    }
}

@Preview(name = "Tablet - Lista", showBackground = true, device = Devices.TABLET)
@Composable
fun PreviewHomeListTablet() {
    MaterialTheme {
        HomeListPane(
            items = mockHomeItems,
            onItemClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(name = "Tablet - Detalle", showBackground = true, device = Devices.TABLET)
@Composable
fun PreviewHomeDetailTablet() {
    MaterialTheme {
        HomeDetailPane(
            item = mockHomeItems.first(),
            onClose = {},
            showCloseButton = false
        )
    }
}
