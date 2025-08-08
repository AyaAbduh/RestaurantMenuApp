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
    LazyColumn {
        items(items) { MenuItemCard(it) }
    }
}

@Composable
fun MenuItemCard(item: MenuItem) {
    Card(Modifier.padding(8.dp).fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(item.image),
                contentDescription = item.name,
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(item.name)
            Text("Price: ${"%.2f".format(item.price)}")
            Text("Rating: ${item.rating}")
            Text(if (item.vegan) "Vegan" else "Non-Vegan")
            Text(if (item.hot) "Spicy" else "Not Spicy")
            if (!item.available) Text("Unavailable")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RestaurantMenuAppTheme {
        MenuScreen(modifier = Modifier.padding())
    }
}