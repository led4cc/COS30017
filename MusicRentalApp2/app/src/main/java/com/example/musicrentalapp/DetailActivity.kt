// DetailActivity.kt
package com.example.musicrentalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicrentalapp.model.Item

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getParcelableExtra<Item>("itemData")
            ?: run { finish(); return }

        setContent {
            var note by remember { mutableStateOf("") }
            var months by remember { mutableStateOf(1) }
            val maxCredit = 100  // example cap for validation
            val total = months * item.pricePerMonth
            val hasError = total > maxCredit || note.isBlank()

            Scaffold { p ->
                Column(Modifier.padding(p).padding(16.dp)) {
                    Text(item.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Price: ${item.pricePerMonth} credits / month")

                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Your note (required)") },
                        isError = note.isBlank()
                    )

                    Spacer(Modifier.height(12.dp))
                    // Simple month picker
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { if (months > 1) months-- }) { Text("-") }
                        Text("$months month(s)", style = MaterialTheme.typography.titleMedium)
                        OutlinedButton(onClick = { months++ }) { Text("+") }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text("Total: $total credits")

                    Spacer(Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = {
                            Toast.makeText(this@DetailActivity, "Booking cancelled", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_CANCELED)
                            finish()
                        }) { Text("Cancel") }

                        Button(onClick = {
                            if (hasError) {
                                // Block leaving until fixed
                                Toast.makeText(this@DetailActivity,
                                    "Fix errors: note required and total ≤ $maxCredit", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@DetailActivity, "Booked successfully!", Toast.LENGTH_SHORT).show()
                                // Return something to show on first screen
                                setResult(RESULT_OK, Intent().putExtra("savedNote", note))
                                finish()
                            }
                        }) { Text("Save") }
                    }
                }
            }
        }
    }
}
