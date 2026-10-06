package co.future.exerciseprogress.ui.workoutdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.theme.HeartRateRed
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.DashedShape
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.Position
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

// The chart is a fixed picture of the whole workout, so it does not scroll or zoom.
private val CHART_HEIGHT = 200.dp

// The y axis starts and ends on a multiple of this many beats per minute, with one more step of room beyond the data.
private const val Y_AXIS_STEP_BPM = 10

// Time labels along the bottom are spaced by the first interval that gives no more than the max count.
private val TIME_LABEL_INTERVALS = listOf(1.minutes, 2.minutes, 5.minutes, 10.minutes, 15.minutes, 30.minutes, 1.hours)
private const val MAX_TIME_LABEL_INTERVALS = 5

private val PEAK_DOT_SIZE = 12.dp

// Room to the right of the last reading, so a time label near the end of the chart isn't cut short.
private val CHART_END_PADDING: (ExtraStore) -> CartesianLayerPadding = {
    CartesianLayerPadding(unscalableEnd = 16.dp)
}

// Readings are one per second, so one step along the x axis is one second.
private val SECONDS_PER_X_STEP: (CartesianChartModel, Double, Double) -> Double = { _, _, _ -> 1.0 }

@Composable
fun HeartRateChartCard(
    chart: HeartRateChartUiState,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MonitorHeart,
                    contentDescription = null,
                    tint = HeartRateRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.heart_rate),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HeartRateChart(chart = chart)
        }
    }
}

@Composable
private fun HeartRateChart(chart: HeartRateChartUiState) {
    val lastSecond = chart.points.last().secondsSinceStart
    val timeAxisTitle = stringResource(R.string.heart_rate_time_axis_title)

    val model = remember(chart) {
        CartesianChartModel(
            LineCartesianLayerModel.build {
                series(
                    x = chart.points.map { it.secondsSinceStart },
                    y = chart.points.map { it.bpm }
                )
            }
        )
    }
    // Labels are plain minute numbers. The axis title says they are minutes, which keeps the labels short.
    val minutesFormatter = remember {
        CartesianValueFormatter { _, secondsSinceStart, _ ->
            secondsSinceStart.seconds.toDouble(DurationUnit.MINUTES).roundToInt().toString()
        }
    }
    val yRange = remember(chart) {
        CartesianLayerRangeProvider.fixed(
            minY = (chart.lowestBpm.roundedDownToStep() - Y_AXIS_STEP_BPM).toDouble(),
            maxY = (chart.peak.bpm.roundedDownToStep() + Y_AXIS_STEP_BPM * 2).toDouble()
        )
    }

    ProvideVicoTheme(rememberM3VicoTheme()) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(Fill(HeartRateRed)),
                            stroke = LineCartesianLayer.LineStroke.Continuous(thickness = 2.dp),
                            pointProvider = rememberPeakPointProvider(chart)
                        )
                    ),
                    rangeProvider = yRange
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = minutesFormatter,
                    guideline = null,
                    itemPlacer = remember(lastSecond) {
                        HorizontalAxis.ItemPlacer.aligned(spacing = { timeLabelSpacingSeconds(lastSecond) })
                    },
                    titleComponent = rememberAxisLabelComponent(),
                    title = { timeAxisTitle }
                ),
                decorations = listOf(rememberPeakLine(chart)),
                layerPadding = CHART_END_PADDING,
                getXStep = SECONDS_PER_X_STEP
            ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
            zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content),
            chartAreaHeight = CHART_HEIGHT
        )
    }
}

// A dashed line across the chart at the peak, labeled with its value.
@Composable
private fun rememberPeakLine(chart: HeartRateChartUiState): HorizontalLine {
    val label = stringResource(R.string.heart_rate_peak_label, chart.peak.bpm)
    val lineComponent = rememberLineComponent(
        fill = Fill(HeartRateRed),
        thickness = 1.dp,
        shape = DashedShape()
    )
    // The backing is the card's color, so the chart's gridlines don't run through the text.
    val labelComponent = rememberTextComponent(
        style = TextStyle(
            color = HeartRateRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        ),
        margins = Insets(horizontal = 4.dp, vertical = 2.dp),
        padding = Insets(horizontal = 4.dp, vertical = 1.dp),
        background = rememberShapeComponent(fill = Fill(CardDefaults.cardColors().containerColor))
    )

    return remember(chart, label, lineComponent, labelComponent) {
        val lastSecond = chart.points.last().secondsSinceStart
        val isPeakInFirstHalf = chart.peak.secondsSinceStart < lastSecond / 2

        HorizontalLine(
            y = { chart.peak.bpm.toDouble() },
            line = lineComponent,
            labelComponent = labelComponent,
            label = { label },
            // The label sits on the side away from the peak, so it doesn't land on top of the dot.
            horizontalLabelPosition = if (isPeakInFirstHalf) {
                Position.Horizontal.End
            } else {
                Position.Horizontal.Start
            }
        )
    }
}

// Draws a dot on the highest reading only. Every other reading is just part of the line.
@Composable
private fun rememberPeakPointProvider(chart: HeartRateChartUiState): LineCartesianLayer.PointProvider {
    val dot = rememberShapeComponent(
        fill = Fill(HeartRateRed),
        shape = CircleShape,
        strokeFill = Fill(MaterialTheme.colorScheme.surface),
        strokeThickness = 2.dp
    )

    return remember(chart, dot) {
        PeakPointProvider(
            peakSeconds = chart.peak.secondsSinceStart,
            point = LineCartesianLayer.Point(component = dot, size = PEAK_DOT_SIZE)
        )
    }
}

private data class PeakPointProvider(
    private val peakSeconds: Int,
    private val point: LineCartesianLayer.Point
) : LineCartesianLayer.PointProvider {

    override fun getPoint(
        entry: LineCartesianLayerModel.Entry,
        extraStore: ExtraStore
    ): LineCartesianLayer.Point? {
        return if (entry.x == peakSeconds.toDouble()) {
            point
        } else {
            null
        }
    }

    override fun getLargestPoint(extraStore: ExtraStore): LineCartesianLayer.Point = point
}

private fun Int.roundedDownToStep(): Int = this / Y_AXIS_STEP_BPM * Y_AXIS_STEP_BPM

private fun timeLabelSpacingSeconds(totalSeconds: Int): Int {
    val chartDuration = totalSeconds.seconds
    val interval = TIME_LABEL_INTERVALS.firstOrNull {
        (chartDuration / it).toInt() <= MAX_TIME_LABEL_INTERVALS
    } ?: TIME_LABEL_INTERVALS.last()

    return interval.inWholeSeconds.toInt()
}
