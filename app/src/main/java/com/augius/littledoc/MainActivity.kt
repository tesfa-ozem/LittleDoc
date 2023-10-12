package com.augius.littledoc

import android.annotation.SuppressLint
import android.app.DatePickerDialog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    var isDatePickerVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                onFilterClick = { isDatePickerVisible = true }
            )
        },
        content = {
            if (isDatePickerVisible) {
                MyDateRangePicker(startDate, endDate) { start, end ->
                    startDate = start
                    endDate = end
                    isDatePickerVisible = false
                }
            } else {
                Text(text = "value")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MediumTopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.Settings, contentDescription = "Icon")
            }
        },
        title = {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = stringResource(id = R.string.insole_data_title),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = stringResource(id = androidx.appcompat.R.string.abc_menu_enter_shortcut_label)
                )
            }
        },
        modifier = modifier.statusBarsPadding()
    )
}

@Composable
fun MyDateRangePicker(
    startDate: Date,
    endDate: Date,
    onDateSelected: (Date, Date) -> Unit
) {
    val context = LocalContext.current
    val startCalendar = Calendar.getInstance()
    startCalendar.time = startDate

    val endCalendar = Calendar.getInstance()
    endCalendar.time = endDate

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            if (startCalendar.before(calendar)) {
                onDateSelected(startDate, calendar.time)
            } else {
                onDateSelected(calendar.time, startDate)
            }
        },
        startCalendar.get(Calendar.YEAR),
        startCalendar.get(Calendar.MONTH),
        startCalendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.maxDate = endCalendar.timeInMillis
    datePickerDialog.show()
}
