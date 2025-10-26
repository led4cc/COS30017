// MainActivity.kt
package com.example.musicrentalapp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicrentalapp.model.Item
import com.example.musicrentalapp.ui.theme.MusicRentalAppTheme
import com.example.musicrentalapp.ui.theme.AppTitleStyle
import com.example.musicrentalapp.ui.theme.PriceStyle
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
// In MainActivity.kt (near other imports)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarBorder



class MainActivity : ComponentActivity() {

    private val items by lazy {
        listOf(
            Item(1, "Scorpion AGS430", 34, 4.5f, listOf("Guitar","Electric","Yamaha"), R.drawable.scorpion),
            Item(2, "Nord Stage 3",    50, 4.8f, listOf("Keyboard","Synth","Nord"),    R.drawable.nordstage),
            Item(3, "Yamaha YAS-280",  42, 4.2f, listOf("Saxophone","Student","Yamaha"), R.drawable.saxophone)
        )
    }
    @Composable
    private fun StarRating(
        rating: Float,
        max: Int = 5,
        modifier: Modifier = Modifier
    ) {
        val full = rating.toInt().coerceIn(0, max)
        val frac = rating - full
        val hasHalf = frac >= 0.25f && frac < 0.75f
        val empty = (max - full - if (hasHalf) 1 else 0).coerceAtLeast(0)

        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            repeat(full) { Icon(Icons.Filled.Star, null, Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary) }
            if (hasHalf) {
                Icon(Icons.Filled.StarHalf, null, Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary)
            }
            repeat(empty) { Icon(Icons.Outlined.StarBorder, null, Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary) }
            Spacer(Modifier.width(6.dp))
            Text(String.format("%.1f / %d", rating, max), style = MaterialTheme.typography.labelMedium)
        }
    }
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun InstrumentCard(
        item: Item,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT: info
                Column(
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(8.dp))
                    CategoryChips(item.categories)
                    Spacer(Modifier.height(8.dp))
                    StarRating(item.rating)

                    Spacer(Modifier.height(12.dp))
                    Text(
                        "${item.pricePerMonth} credits / month",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // RIGHT: image
                Box(
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(item.imageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun CategoryChips(tags: List<String>) {
        Column {
            Text("Categories:", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach { tag ->
                    FilterChip(
                        selected = true, // display-only
                        onClick = { /* no-op */ },
                        label = { Text(tag) }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicRentalAppTheme {
                var index by remember { mutableStateOf(0) }
                var savedNote by remember { mutableStateOf("") }
                val item = items[index]

                // Activity result launcher for DetailActivity
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { res ->
                    if (res.resultCode == Activity.RESULT_OK) {
                        savedNote = res.data?.getStringExtra("savedNote").orEmpty()
                    } else {

                    }
                }

                val gradientBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFF6A4CFC), Color(0xFFF5B800))
                )

                Surface(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(gradientBrush)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Title
                        Text(
                            "Choose your favourite instrument",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Spacer(Modifier.height(16.dp))

                        // Card with info-left / image-right
                        InstrumentCard(
                            item = item,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Optional: show saved note chip under the card
                        if (savedNote.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            AssistChip(onClick = {}, label = { Text("Saved: $savedNote") })
                        }

                        Spacer(Modifier.height(20.dp))

                        // Buttons under the card
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = {
                                index = (index + 1) % items.size
                            }) { Text("Next") }

                            Button(onClick = {
                                launcher.launch(
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
