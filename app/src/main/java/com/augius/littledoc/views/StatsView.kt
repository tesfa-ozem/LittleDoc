package com.augius.littledoc.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.augius.littledoc.ui.theme.LittleDocTheme

class StatsView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LittleDocTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleDocTheme {
        RunStatsPage()
    }
}

data class RunStats(val date: String, val distance: Double, val time: String)

@Composable
fun RunStatsPage() {
    var runs by remember { mutableStateOf(dummyData()) }
    var filter by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = filter,
            onValueChange = { filter = it },
            label = { Text("Filter by date") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle filtering here
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LazyColumn {
            items(runs.filter { it.date.contains(filter, ignoreCase = true) }) { run ->
                RunStatsItem(run)
            }
        }
    }
}

@Composable
fun RunStatsItem(run: RunStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Date: ${run.date}", fontSize = 18.sp)
            Text(text = "Distance: ${run.distance} km", fontSize = 16.sp)
            Text(text = "Time: ${run.time}", fontSize = 16.sp)
        }
    }
}

fun dummyData(): List<RunStats> {
    return listOf(
        RunStats("2023-10-01", 5.2, "30:45"),
        RunStats("2023-09-28", 6.1, "35:20"),
        RunStats("2023-09-25", 4.7, "28:15"),
        RunStats("2023-09-20", 7.3, "42:10"),
        RunStats("2023-09-18", 3.5, "21:30"),
        RunStats("2023-09-15", 8.2, "48:55"),
    )
}