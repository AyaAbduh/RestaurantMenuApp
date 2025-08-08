package com.example.restaurantmenuapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.restaurantmenuapp.data.model.MenuItem
import com.example.restaurantmenuapp.ui.theme.RestaurantMenuAppTheme
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantMenuAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val items by viewModel.items.collectAsState()
    LaunchedEffect(Unit) { viewModel.load() }

    var expanded by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf("Price ↑") }

    Column(modifier.fillMaxSize()) {
        // Sorting row
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = { expanded = true }) {
                Text("Sort by: $selectedSort")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val sortOptions = listOf(
                    "Price ↑", "Price ↓",
                    "Rating ↑", "Rating ↓",
                    "Vegan", "Hot", "Available"
                )
                sortOptions.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            selectedSort = opt
                            expanded = false
                            viewModel.sortItemsBy(opt)
                        }
                    )
                }
            }
        }

        // List
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No items to show")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { MenuItemCard(it) }
            }
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItem) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(item.image),
                contentDescription = item.name,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(item.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("EGP ${"%.2f".format(item.price)}")
                    Text("★ ${"%.1f".format(item.rating)}")
                    if (item.vegan) Text("Vegan")
                    if (item.hot) Text("Hot")
                    if (!item.available) Text("Unavailable", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

/* ---------- Preview (no ViewModel needed) ---------- */
@Preview(showBackground = true, name = "Menu Screen (Light)")
@Preview(showBackground = true, name = "Menu Screen (Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MenuScreenPreview() {
    val sample = listOf(
        MenuItem(101, "Classic Beef Burger", "Grilled patty with cheese", 8.99, "https://picsum.photos/300/200?1", 4.2f, false, false, true, "hamburgers"),
        MenuItem(202, "Arrabbiata Penne", "Spicy tomato sauce", 9.75, "https://picsum.photos/300/200?2", 4.6f, true, true, true, "pasta"),
        MenuItem(301, "Lemon Iced Tea", "Refreshing", 3.50, "https://picsum.photos/300/200?3", 4.0f, true, false, true, "drinks")
    )
    RestaurantMenuAppTheme {
        Column {
            // small local state to demo the sort dropdown in preview
            var items by remember { mutableStateOf(sample) }
            var expanded by remember { mutableStateOf(false) }
            var selectedSort by remember { mutableStateOf("Price ↑") }

            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { expanded = true }) { Text("Sort by: $selectedSort") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    val opts = listOf("Price ↑","Price ↓","Rating ↑","Rating ↓","Vegan","Hot","Available")
                    opts.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                selectedSort = opt; expanded = false
                                items = when (opt) {
                                    "Price ↑"   -> items.sortedBy { it.price }
                                    "Price ↓"   -> items.sortedByDescending { it.price }
                                    "Rating ↑"  -> items.sortedBy { it.rating }
                                    "Rating ↓"  -> items.sortedByDescending { it.rating }
                                    "Vegan"     -> items.sortedByDescending { it.vegan }
                                    "Hot"       -> items.sortedByDescending { it.hot }
                                    "Available" -> items.sortedByDescending { it.available }
                                    else -> items
                                }
                            }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) { items(items) { MenuItemCard(it) } }
        }
    }
}
