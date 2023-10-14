package com.augius.littledoc

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.augius.littledoc.ui.theme.LittleDocTheme
import com.augius.littledoc.views.RunStatsPage
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.createHorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.axis.vertical.createVerticalAxis
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes.pillShape
import com.patrykandpatrick.vico.core.component.shape.Shapes.rectShape
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.legend.Legend

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleDocTheme {
        HomeScreen()
    }
}


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
        content = { paddingValues ->

            LazyColumn(

                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                item{MyDateRangePicker()}
                item{HeatmapFoot()}
                item {
                    temperatureChart()
                    }
                item{humidityChart()}


            }
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDateRangePicker(

) {
    val calendar = Calendar.getInstance()
    calendar.set(1990, 0, 22) // add year, month (Jan), date

    // set the initial date
    val state = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input)
    DateRangePicker(state = state, showModeToggle = false,)

    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ROOT)
    /*Text(
        text = "Selected date: ${formatter.format(Date(datePickerState.selectedDateMillis!!))}"
    )*/
}


@Composable
fun HeatmapFoot() {
    val heatmapPainter = painterResource(id = R.drawable.two_feet) // Replace with your actual heatmap image resource
    Image(
        painter = heatmapPainter,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(0.4F)
    )
}
fun getRandomEntries() = List(4) { entryOf(it, Random.nextFloat() * 16f) }
@Composable
fun temperatureChart(){
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(4.dp),

    ){
        Column (modifier = Modifier.padding(8.dp),){
            Text(text = "Temperature")
        }
        Chart(
            modifier = Modifier.height(200.dp),
            chart = lineChart(

                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minX = 0f,
                    maxY = 3f,
                ),
            ),

            model = entryModelOf(entriesOf(2.5f, 3f, 1.7f, 3f), entriesOf(1f, 2.2f, 1.3f, 3f)),
            endAxis = createVerticalAxis {
                label = textComponent(

                    textSize = 10.sp,

                    padding = dimensionsOf(horizontal = 4.dp, vertical = 2.dp),
                )
                axis = null
                tick = null
                guideline = LineComponent(
                    color = Color.LightGray.toArgb(),
                    thicknessDp = 1.dp.value,
                    shape = DashedShape(
                        shape = pillShape,
                        dashLengthDp = 2.dp.value,
                        gapLengthDp = 4.dp.value,
                    ),
                )
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside
            },
            bottomAxis = createHorizontalAxis {
                label = textComponent(

                    textSize = 10.sp,

                    padding = dimensionsOf(horizontal = 4.dp, vertical = 2.dp),
                )
                tick = null
                guideline = null
                axis = lineComponent(color = Color.LightGray, thickness = 1.dp)
            },
        )
    }


}

@Composable
fun humidityChart(){
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(4.dp),

        ){
        Column (modifier = Modifier.padding(8.dp),){
            Text(text = "Humidity")
        }
        Chart(
            modifier = Modifier.height(200.dp),
            chart = lineChart(

                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minX = 0f,
                    maxY = 3f,
                ),
            ),
            model = entryModelOf(entriesOf(1f, 2f, 1.3f, 3f), entriesOf(2f, 2.2f, 1.3f, 2f)),
            endAxis = createVerticalAxis {
                label = textComponent(

                    textSize = 10.sp,

                    padding = dimensionsOf(horizontal = 4.dp, vertical = 2.dp),
                )
                axis = null
                tick = null
                guideline = LineComponent(
                    color = Color.LightGray.toArgb(),
                    thicknessDp = 1.dp.value,
                    shape = DashedShape(
                        shape = pillShape,
                        dashLengthDp = 2.dp.value,
                        gapLengthDp = 4.dp.value,
                    ),
                )
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside
            },
            bottomAxis = createHorizontalAxis {
                label = textComponent(

                    textSize = 10.sp,

                    padding = dimensionsOf(horizontal = 4.dp, vertical = 2.dp),
                )
                tick = null
                guideline = null
                axis = lineComponent(color = Color.LightGray, thickness = 1.dp)
            },
        )
    }
}

