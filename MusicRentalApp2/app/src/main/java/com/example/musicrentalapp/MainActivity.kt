// MainActivity.kt
package com.example.musicrentalapp

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicrentalapp.model.Item
import com.example.musicrentalapp.ui.theme.MusicRentalAppTheme

class MainActivity : ComponentActivity() {

    private val items by lazy {
        listOf(
            Item(1, "Scorpion AGS430", 34, 4.5f, "Guitar", R.drawable.product_image),
            Item(2, "Nord Stage 3", 50, 4.8f, "Keyboard", R.drawable.product_image),
            Item(3, "Yamaha YAS-280", 42, 4.2f, "Saxophone", R.drawable.product_image)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicRentalAppTheme {
                // receive saved note from DetailActivity (optional)
                val savedNote = intent.getStringExtra("savedNote") ?: ""
                var index by remember { mutableStateOf(0) }
                val item = items[index]
                val gradientBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFF6A4CFC), Color(0xFFF5B800)),
                )

                Surface(Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxSize().background(gradientBrush).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painterResource(item.imageRes), contentDescription = null)

                        Spacer(Modifier.height(12.dp))
                        Text(item.name, style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(4.dp))
                        Text("${item.pricePerMonth} credits / month",
                            style = MaterialTheme.typography.bodyLarge)

                        Spacer(Modifier.height(8.dp))
                        // Non-TextView widget: Chip for category
                        FilterChip(
                            selected = true,
                            onClick = { /* no-op */ },
                            label = { Text(item.category) }
                        )

                        if (savedNote.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            AssistChip(onClick = {}, label = { Text("Saved: $savedNote") })
                        }

                        Spacer(Modifier.height(24.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = {
                                index = (index + 1) % items.size
                            }) { Text("Next") }

                            Button(onClick = {
                                startActivity(
                                    Intent(this@MainActivity, DetailActivity::class.java)
                                        .putExtra("itemData", item)
                                )
                            }) { Text("Borrow") }
                        }
                    }
                }
            }
        }
    }
}
