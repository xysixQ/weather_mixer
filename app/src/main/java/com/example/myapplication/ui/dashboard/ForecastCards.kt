package com.weathermixer.sixq

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.graphics.Paint as AndroidPaint
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedImageDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibrationAttributes
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ImageView
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable as ComposeAnimatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.coroutines.resume


@Composable
internal fun HorizontalSwipeIndicator(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
    Canvas(modifier = modifier.size(42.dp, 18.dp)) {
        val stroke = 1.8.dp.toPx()
        val centerY = size.height / 2f
        val inset = 4.dp.toPx()
        val arm = 4.dp.toPx()
        drawLine(color, Offset(inset + arm, centerY - arm), Offset(inset, centerY), stroke, StrokeCap.Round)
        drawLine(color, Offset(inset, centerY), Offset(inset + arm, centerY + arm), stroke, StrokeCap.Round)
        drawCircle(color, radius = 1.8.dp.toPx(), center = Offset(size.width / 2f, centerY))
        drawLine(color, Offset(size.width - inset - arm, centerY - arm), Offset(size.width - inset, centerY), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width - inset, centerY), Offset(size.width - inset - arm, centerY + arm), stroke, StrokeCap.Round)
    }
}

@Composable
internal fun DailyForecastCard(
    days: List<DailyForecast>,
    onClick: () -> Unit,
    onDayClick: (DailyForecast) -> Unit,
) {
    val temperatureUnit = LocalTemperatureUnit.current
    val itemWidth = 74.dp
    val chartWidth = itemWidth * days.size
    Card(
        modifier = Modifier.fillMaxWidth().floatingCardMotion().clip(RoundedCornerShape(30.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        ),
    ) {
        Column(modifier = Modifier.padding(vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Today, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("每日预报", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                HorizontalSwipeIndicator()
            }
            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
                    .width(chartWidth),
            ) {
                Column(modifier = Modifier.width(chartWidth)) {
                    Row(modifier = Modifier.width(chartWidth)) {
                        days.forEach { day ->
                            Column(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .padding(vertical = 3.dp)
                                    .graphicsLayer { alpha = if (day.isYesterday) 0.52f else 1f },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Box(
                                    modifier = Modifier.height(34.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        forecastDayLabel(day),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                                WeatherMiniIcon(day.condition, compact = true)
                                Text(day.rainProbability?.let { "${it.roundToInt()}%" } ?: " ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    TemperatureLineChart(
                        modifier = Modifier.width(chartWidth).height(112.dp),
                        primary = days.map { temperatureUnit.convert(it.highC) },
                        primaryLabels = days.map { temperatureUnit.format(it.highC) },
                        secondary = days.map { temperatureUnit.convert(it.lowC) },
                        secondaryLabels = days.map { temperatureUnit.format(it.lowC) },
                        fadedIndices = days.mapIndexedNotNull { index, day ->
                            index.takeIf { day.isYesterday }
                        }.toSet(),
                    )
                    Row(modifier = Modifier.width(chartWidth)) {
                        days.forEach { day ->
                            Box(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .height(34.dp)
                                    .graphicsLayer { alpha = if (day.isYesterday) 0.52f else 1f },
                                contentAlignment = Alignment.Center,
                            ) {
                                WeatherMiniIcon(day.condition, isNight = true, compact = true)
                            }
                        }
                    }
                }
                Row(modifier = Modifier.matchParentSize()) {
                    days.forEachIndexed { index, day ->
                        Box(
                            modifier = Modifier
                                .width(itemWidth)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onDayClick(day) },
                        ) {
                            if (index > 0) {
                                Box(
                                    Modifier
                                        .align(Alignment.CenterStart)
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.48f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun HourlyForecastCard(
    hours: List<HourlyForecast>,
    astronomy: AstronomyInfo,
    longitude: Double,
    onClick: () -> Unit,
    onHourClick: (HourlyForecast) -> Unit,
) {
    val temperatureUnit = LocalTemperatureUnit.current
    val visibleHours = hours.take(24)
    val itemWidth = 68.dp
    val chartWidth = itemWidth * visibleHours.size
    Card(
        modifier = Modifier.fillMaxWidth().floatingCardMotion(80).clip(RoundedCornerShape(30.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        ),
    ) {
        Column(modifier = Modifier.padding(vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(8.dp))
                Text("逐小时预报", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                HorizontalSwipeIndicator()
            }
            Column(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
                    .width(chartWidth),
            ) {
                Row(modifier = Modifier.width(chartWidth)) {
                    visibleHours.forEach { hour ->
                        Column(
                            modifier = Modifier
                                .width(itemWidth)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onHourClick(hour) }
                                .padding(vertical = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(formatForecastTime(hour.timeMillis), style = MaterialTheme.typography.labelMedium)
                            WeatherMiniIcon(
                                condition = hour.condition,
                                isNight = isNightTime(hour.timeMillis, astronomy, longitude),
                                compact = true,
                            )
                            Text(hour.rainProbability?.let { "${it.roundToInt()}%" } ?: " ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(
                                text = hour.windKph?.let { "${it.roundToInt()}km/h" }.orEmpty(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                TemperatureLineChart(
                    modifier = Modifier.width(chartWidth).height(118.dp),
                    primary = visibleHours.map { temperatureUnit.convert(it.temperatureC) },
                    primaryLabels = visibleHours.map { temperatureUnit.format(it.temperatureC) },
                )
                Row(modifier = Modifier.width(chartWidth)) {
                    visibleHours.forEach { hour ->
                        Box(modifier = Modifier.width(itemWidth), contentAlignment = Alignment.Center) {
                            Text(
                                text = hour.aqi?.let { "AQI $it" }.orEmpty(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TemperatureLineChart(
    modifier: Modifier,
    primary: List<Double>,
    primaryLabels: List<String>,
    secondary: List<Double>? = null,
    secondaryLabels: List<String>? = null,
    fadedIndices: Set<Int> = emptySet(),
) {
    val allValues = primary + secondary.orEmpty()
    val low = (allValues.minOrNull() ?: 0.0) - 1.0
    val high = (allValues.maxOrNull() ?: 1.0) + 1.0
    val range = max(1.0, high - low)
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.tertiary
    Canvas(modifier = modifier) {
        fun drawSeries(
            values: List<Double>,
            labels: List<String>,
            color: Color,
            fillArea: Boolean,
            labelsBelow: Boolean,
        ) {
            if (values.isEmpty()) return
            val step = size.width / values.size
            val path = Path()
            val points = values.mapIndexed { index, value ->
                val x = step * index + step / 2f
                val y = size.height - ((value - low) / range * size.height * 0.78 + size.height * 0.11).toFloat()
                Offset(x, y)
            }
            points.forEachIndexed { index, point ->
                val x = point.x
                val y = point.y
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            if (fillArea) {
                val areaPath = Path().apply {
                    points.forEachIndexed { index, point ->
                        if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, size.height)
                    lineTo(points.first().x, size.height)
                    close()
                }
                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF7CCBFF).copy(alpha = 0.30f), Color.Transparent),
                        startY = 0f,
                        endY = size.height,
                    ),
                )
            }
            points.zipWithNext().forEachIndexed { index, (start, end) ->
                val fadedSegment = index in fadedIndices || (index + 1) in fadedIndices
                drawLine(
                    color = color.copy(alpha = if (fadedSegment) 0.42f else 1f),
                    start = start,
                    end = end,
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
            val labelPaint = AndroidPaint(AndroidPaint.ANTI_ALIAS_FLAG).apply {
                this.color = color.toArgb()
                textAlign = AndroidPaint.Align.CENTER
                textSize = 11.sp.toPx()
                isFakeBoldText = true
            }
            points.forEachIndexed { index, point ->
                labelPaint.color = color.copy(
                    alpha = if (index in fadedIndices) 0.48f else 1f,
                ).toArgb()
                val baseline = if (labelsBelow) {
                    (point.y + 17.dp.toPx()).coerceAtMost(size.height - 2.dp.toPx())
                } else {
                    (point.y - 7.dp.toPx()).coerceAtLeast(11.dp.toPx())
                }
                drawContext.canvas.nativeCanvas.drawText(
                    labels.getOrElse(index) { values[index].roundToInt().toString() },
                    point.x,
                    baseline,
                    labelPaint,
                )
            }
        }
        drawSeries(primary, primaryLabels, primaryColor, fillArea = true, labelsBelow = false)
        secondary?.let {
            drawSeries(
                values = it,
                labels = secondaryLabels.orEmpty(),
                color = secondaryColor,
                fillArea = false,
                labelsBelow = true,
            )
        }
    }
}

@Composable
internal fun AstronomyCard(info: AstronomyInfo, longitude: Double, onClick: () -> Unit) {
    val sunPosition = astronomyProgress(info.sunrise, info.sunset, longitude)
    val moonPosition = astronomyProgress(info.moonrise, info.moonset, longitude)
    val sunProgress by animateFloatAsState(
        targetValue = sunPosition ?: 0f,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label = "sunArc",
    )
    val moonProgress by animateFloatAsState(
        targetValue = moonPosition ?: 0f,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label = "moonArc",
    )
    Card(
        modifier = Modifier.fillMaxWidth().floatingCardMotion(120).clip(RoundedCornerShape(30.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        ),
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("日月升落", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            AstronomyArc(
                "太阳",
                info.sunrise,
                info.sunset,
                AstronomyBody.Sun,
                sunProgress,
                MaterialTheme.colorScheme.tertiary,
                showBody = sunPosition != null,
            )
            AstronomyArc(
                label = "月亮",
                rise = info.moonrise,
                set = info.moonset,
                body = AstronomyBody.Moon,
                progress = moonProgress,
                color = MaterialTheme.colorScheme.primary,
                annotation = "月相 · ${info.moonPhase ?: "暂无数据"}",
                showBody = moonPosition != null,
            )
        }
    }
}

internal enum class AstronomyBody { Sun, Moon }

@Composable
internal fun MonochromeAstronomyGlyph(
    body: AstronomyBody,
    modifier: Modifier = Modifier,
) {
    val glyphColor = MaterialTheme.colorScheme.onSurface
    Canvas(modifier) {
        when (body) {
            AstronomyBody.Sun -> {
                val center = Offset(size.width / 2f, size.height / 2f)
                val stroke = max(1.7.dp.toPx(), size.minDimension * 0.085f)
                drawCircle(
                    color = glyphColor,
                    radius = size.minDimension * 0.22f,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = StrokeCap.Round),
                )
                repeat(8) { index ->
                    val angle = index * Math.PI * 2.0 / 8.0
                    val startRadius = size.minDimension * 0.34f
                    val endRadius = size.minDimension * 0.46f
                    drawLine(
                        color = glyphColor,
                        start = Offset(
                            center.x + cos(angle).toFloat() * startRadius,
                            center.y + sin(angle).toFloat() * startRadius,
                        ),
                        end = Offset(
                            center.x + cos(angle).toFloat() * endRadius,
                            center.y + sin(angle).toFloat() * endRadius,
                        ),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                }
            }
            AstronomyBody.Moon -> {
                val path = Path().apply {
                    moveTo(size.width * 0.70f, size.height * 0.08f)
                    cubicTo(
                        size.width * 0.35f, size.height * 0.15f,
                        size.width * 0.16f, size.height * 0.45f,
                        size.width * 0.24f, size.height * 0.72f,
                    )
                    cubicTo(
                        size.width * 0.31f, size.height * 0.98f,
                        size.width * 0.65f, size.height * 1.02f,
                        size.width * 0.88f, size.height * 0.80f,
                    )
                    cubicTo(
                        size.width * 0.59f, size.height * 0.84f,
                        size.width * 0.39f, size.height * 0.64f,
                        size.width * 0.38f, size.height * 0.43f,
                    )
                    cubicTo(
                        size.width * 0.37f, size.height * 0.25f,
                        size.width * 0.49f, size.height * 0.12f,
                        size.width * 0.70f, size.height * 0.08f,
                    )
                    close()
                }
                drawPath(path, glyphColor)
            }
        }
    }
}

@Composable
internal fun AstronomyArc(
    label: String,
    rise: String?,
    set: String?,
    body: AstronomyBody,
    progress: Float,
    color: Color,
    annotation: String? = null,
    showBody: Boolean = true,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MonochromeAstronomyGlyph(body, Modifier.size(21.dp))
            Spacer(Modifier.width(7.dp))
            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(58.dp)) {
            val t = progress.coerceIn(0f, 1f)
            val oneMinus = 1f - t
            val startX = 12.dp
            val endX = maxWidth - 12.dp
            val baseline = maxHeight - 8.dp
            val controlX = maxWidth / 2f
            val controlY = -maxHeight * 0.45f
            val iconX = startX * (oneMinus * oneMinus) +
                controlX * (2f * oneMinus * t) +
                endX * (t * t)
            val iconY = baseline * (oneMinus * oneMinus) +
                controlY * (2f * oneMinus * t) +
                baseline * (t * t)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val start = Offset(12.dp.toPx(), size.height - 8.dp.toPx())
                val end = Offset(size.width - 12.dp.toPx(), size.height - 8.dp.toPx())
                val control = Offset(size.width / 2f, -size.height * 0.45f)
                fun pointAt(fraction: Float): Offset {
                    val remaining = 1f - fraction
                    return Offset(
                        x = start.x * remaining * remaining + control.x * 2f * remaining * fraction + end.x * fraction * fraction,
                        y = start.y * remaining * remaining + control.y * 2f * remaining * fraction + end.y * fraction * fraction,
                    )
                }
                val travelledPath = Path().apply { moveTo(start.x, start.y) }
                val remainingStart = pointAt(t)
                val remainingPath = Path().apply { moveTo(remainingStart.x, remainingStart.y) }
                val steps = 64
                repeat(steps) { index ->
                    val fraction = (index + 1) / steps.toFloat()
                    val point = pointAt(fraction)
                    if (fraction <= t) travelledPath.lineTo(point.x, point.y)
                    if (fraction > t) remainingPath.lineTo(point.x, point.y)
                }
                if (t in 0.001f..0.999f) {
                    travelledPath.lineTo(remainingStart.x, remainingStart.y)
                }
                drawPath(
                    remainingPath,
                    color.copy(alpha = 0.25f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(7.dp.toPx(), 6.dp.toPx())),
                    ),
                )
                drawPath(
                    travelledPath,
                    color.copy(alpha = 0.82f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                )
            }
            if (showBody) {
                Icon(
                    imageVector = if (body == AstronomyBody.Sun) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .offset(x = iconX - 11.dp, y = iconY - 11.dp)
                        .size(23.dp),
                )
            }
        }
        annotation?.let {
            Text(
                text = it,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("升 ${rise ?: "--:--"}", style = MaterialTheme.typography.bodySmall)
            Text("落 ${set ?: "--:--"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

internal fun astronomyProgress(rise: String?, set: String?, longitude: Double): Float? {
    val riseMinutes = clockToMinutes(rise) ?: return null
    val setMinutes = clockToMinutes(set) ?: return null
    val utcClock = SimpleDateFormat("HH:mm", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date())
    val utcMinutes = clockToMinutes(utcClock) ?: return null
    val currentMinutes = Math.floorMod(
        utcMinutes + (longitude / 15.0 * 60.0).roundToInt(),
        24 * 60,
    )
    if (setMinutes > riseMinutes) {
        if (currentMinutes !in riseMinutes..setMinutes) return null
        return ((currentMinutes - riseMinutes).toFloat() / (setMinutes - riseMinutes))
            .coerceIn(0f, 1f)
    }
    val adjustedSet = setMinutes + 24 * 60
    val adjustedCurrent = when {
        currentMinutes >= riseMinutes -> currentMinutes
        currentMinutes <= setMinutes -> currentMinutes + 24 * 60
        else -> return null
    }
    return ((adjustedCurrent - riseMinutes).toFloat() / (adjustedSet - riseMinutes))
        .coerceIn(0f, 1f)
}

internal fun forecastDayLabel(day: DailyForecast): String {
    if (day.isYesterday) return "昨天"
    val today = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
    if (SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date(day.timeMillis)) == today) return "今天"
    return SimpleDateFormat("E\nM/d", Locale.CHINA).format(Date(day.timeMillis))
}

internal fun formatForecastTime(timeMillis: Long): String = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(timeMillis))
