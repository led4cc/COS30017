// DetailActivity.kt
package com.example.musicrentalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicrentalapp.model.Item
import com.example.musicrentalapp.ui.theme.AppTitleStyle
import com.example.musicrentalapp.ui.theme.PriceStyle
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.RatingBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<Item>("itemData")
            ?: run { finish(); return }

        setContent {
            var note by remember { mutableStateOf("") }
            var months by remember { mutableIntStateOf(1) }
            val maxCredit = 100
            val total = months * item.pricePerMonth
            val hasError = total > maxCredit || note.isBlank()

            // Back = cancel + feedback
            BackHandler {
                Toast.makeText(this@DetailActivity, "Booking cancelled", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Borrow ${item.name}") },
                        navigationIcon = {
                            TextButton(onClick = {
                                Toast.makeText(this@DetailActivity, "Booking cancelled", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_CANCELED)
                                finish()
                            }) { Text("Back") }
                        }
                    )
                }
            ) { p ->
                Column(Modifier.padding(p).padding(16.dp)) {
                    // 1) Show image on detail screen too
                    Image(painterResource(item.imageRes), contentDescription = null)
                    Spacer(Modifier.height(12.dp))

                    Text(item.name, style = AppTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("Price: ${item.pricePerMonth} credits / month", style = PriceStyle)
                    Text("Rating: ${"%.1f".format(item.rating)} / 5.0")



                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Your note (required)") },
                        isError = note.isBlank(),
                        supportingText = {
                            if (note.isBlank()) Text("Note is required")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))
                    // 2) Non-TextView widget: Slider for months (1..12)
                    Text("Months: $months")
                    Slider(
                        value = months.toFloat(),
                        onValueChange = { months = it.toInt().coerceIn(1, 12) },
                        valueRange = 1f..12f,
                        steps = 10, // 11 ticks total => 1..12
                        modifier = Modifier.testTag("durationSlider")

                    )

                    Spacer(Modifier.height(8.dp))
                    Text("Total: $total credits" + if (total > maxCredit) " (exceeds $maxCredit!)" else "")

                    Spacer(Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = {
                            Toast.makeText(this@DetailActivity, "Booking cancelled", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_CANCELED)
                            finish()
                        }) { Text("Cancel") }

                        Button(
                            enabled = !hasError,
                            onClick = {
                                Toast.makeText(this@DetailActivity, "Booked successfully!", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK, Intent().putExtra("savedNote", note))
                                finish()
                            }
                        ) { Text("Save") }
                    }
                }
            }
        }
    }
}
